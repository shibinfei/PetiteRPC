package io.hahahahaha.petiterpc.provider;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import io.hahahahaha.petiterpc.common.PetiteException;
import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.transport.TransportChannel;

public class ProviderLimitTask extends HystrixCommand<Void> {
	
    private ProviderMediator providerMediator = new ProviderMediator();
    
	private Request request;

	private TransportChannel channel;

	public ProviderLimitTask(TransportChannel channel, Request request) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(request.getInterfaceClass().getName()))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)
						.withExecutionTimeoutEnabled(true)
						.withExecutionIsolationThreadInterruptOnTimeout(true)
						.withCircuitBreakerEnabled(true)
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
		providerMediator.handleRequest(channel, request);
		return null;
	}
	
	@Override
	protected Void getFallback() {
		Response response = new Response();
		response.setRequestId(request.getRequestId());
		response.setResult(new PetiteException("Call limited"));
		channel.write(response);
		return null;
	}

}
