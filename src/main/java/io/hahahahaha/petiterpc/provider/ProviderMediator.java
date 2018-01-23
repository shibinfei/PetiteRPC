package io.hahahahaha.petiterpc.provider;

import java.nio.file.ProviderNotFoundException;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;

import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.transport.TransportChannel;

/**
 * @author shibinfei
 *
 */
public class ProviderMediator {

    private ProviderContainer providerContainer = ProviderContainer.getInstance();
    
    public void handleRequest(TransportChannel channel, Request request) {

        Response response = new Response();
        
        Class<?> interfaceClass = request.getInterfaceClass();

        response.setRequestId(request.getRequestId());
        
        Object providerInstance = providerContainer.getProvider(interfaceClass);
        
        if (providerInstance == null) {
            response.setResult(new ProviderNotFoundException("No Provider For " + request.getClass().getName()));
            channel.write(response);
            return;
        }
        
        MethodAccess methodAccess = methodAccessCache.computeIfAbsent(interfaceClass, key -> MethodAccess.get(key));
        try {
            Object result = methodAccess.invoke(providerInstance, request.getMethodName(), request.getArgs());
            response.setResult(result);
        } catch (Throwable t) {
            response.setResult(t);
        }
        
        channel.write(response);
        return;
    }
    
    private ConcurrentMap<Class<?>, MethodAccess> methodAccessCache = Maps.newConcurrentMap();
    
}
