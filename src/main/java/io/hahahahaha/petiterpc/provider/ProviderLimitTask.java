package io.hahahahaha.petiterpc.provider;

import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import io.hahahahaha.petiterpc.common.PetiteException;
import io.hahahahaha.petiterpc.common.Provider;
import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.transport.TransportChannel;
/**
 * 
 * @author shibinfei
 * TODO not support overload
 * TODO not support void method
 */
public class ProviderLimitTask extends HystrixCommand<Void> {
	
	private ConcurrentMap<Class<?>, MethodAccess> methodAccessCache = Maps.newConcurrentMap();
	
    private ProviderContainer providerContainer = ProviderContainer.getInstance();
	
	private Request request;

	private TransportChannel channel;

	public ProviderLimitTask(TransportChannel channel, Request request) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(request.getInterfaceClass().getName()))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)
						.withExecutionTimeoutEnabled(true)
						.withExecutionIsolationThreadInterruptOnTimeout(true)
						.withCircuitBreakerEnabled(true)
						.withCircuitBreakerRequestVolumeThreshold(5)
						.withCircuitBreakerSleepWindowInMilliseconds(1000)
						.withCircuitBreakerErrorThresholdPercentage(50)
						)
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(1)
						.withMaxQueueSize(1))
				.andCommandKey(HystrixCommandKey.Factory
						.asKey(request.getInterfaceClass().getName() + "_" + request.getMethodName()))
				);
		this.request = request;
		this.channel = channel;
	}

	protected ProviderLimitTask(HystrixCommandGroupKey group) {
		super(group);
	}

	@Override
	protected Void run() throws Exception {
        Response response = new Response();
        
        Class<?> interfaceClass = request.getInterfaceClass();

        response.setRequestId(request.getRequestId());
        
        Object providerInstance = providerContainer.getProvider(interfaceClass);
        
        if (providerInstance == null) {
            response.setResult(new PetiteException("No Provider For " + request.getClass().getName()));
            channel.write(response);
            return null;
        }
        
        MethodAccess methodAccess = methodAccessCache.computeIfAbsent(interfaceClass, key -> MethodAccess.get(key));
        try {
        		int methodIndex = getMethodIndex(methodAccess);
        		Object result = methodAccess.invoke(providerInstance, methodIndex, request.getArgs());
            response.setResult(result);
        } catch (Throwable t) {
            response.setResult(t);
        }
        
        channel.write(response);
		return null;
	}
	
	@Override
	protected Void getFallback() {

		Response response = new Response();
		response.setRequestId(request.getRequestId());
		
        Object providerInstance = providerContainer.getProvider(request.getInterfaceClass());
        
        if (providerInstance == null) {
            response.setResult(new PetiteException("No Provider For " + request.getClass().getName()));
            channel.write(response);
            return null;
        }
        
        Provider providerAnnotation = providerInstance.getClass().getAnnotation(Provider.class);
        
        boolean degradation = providerAnnotation.degradation();
        
        if (degradation) {
        		MethodAccess methodAccess = methodAccessCache.computeIfAbsent(request.getInterfaceClass(), key -> MethodAccess.get(key));
        		response.setResult(getMockResult(methodAccess));
        } else {
        		response.setResult(new PetiteException("Call limited"));
        }
        
		channel.write(response);
		return null;
	}
	
	
	private int getMethodIndex(MethodAccess methodAccess) {
		return methodAccess.getIndex(request.getMethodName(), request.getArgTypes());
	}
	
	private Object getMockResult(MethodAccess methodAccess) {
		
		int methodIndex = getMethodIndex(methodAccess);
		
		Class<?> returnType = methodAccess.getReturnTypes()[methodIndex];
		
		if (returnType == void.class) {
			return null;
		} else if (returnType == int.class) {
			return 0;
		} else if (returnType == char.class) {
			return ' ';
		} else if (returnType == short.class) {
			return (short)0;
		} else if (returnType == byte.class) {
			return (byte)0;
		} else if (returnType == long.class) {
			return 0;
		} else if (returnType == boolean.class) {
			return false;
		} else if (returnType == float.class) {
			return 0.0;
		} else if (returnType == double.class) {
			return 0.0;
		}
		
		return null;
	}
	
	
}
