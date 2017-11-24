package io.hahahahaha.petiterpc.server;

import java.nio.file.ProviderNotFoundException;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;

import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.transport.TransportChannel;
import server.ProviderContainer;

/**
 * @author shibinfei
 *
 */
public class ServerMediator {

    ProviderContainer providerContainer;
    
    public void handleRequest(TransportChannel channel, Request request) {

        Response response = new Response();
        
        Class<?> interfaceClass = request.getInterfaceClass();

        response.setRequestId(request.getRequestId());
        
        Object providerInstance = providerContainer.getProvider(interfaceClass);
        
        if (providerInstance == null) {
            response.setResult(new ProviderNotFoundException("No Provider For " + request.getInterfaceName()));
            channel.write(response);
            return;
        }
        
        try {
            MethodAccess methodAccess = methodAccessCache.computeIfAbsent(interfaceClass, key -> MethodAccess.get(key));
            Object result = methodAccess.invoke(providerInstance, request.getMethodName(), request.getArgs());  // TODO 更多优化空间
            response.setResult(result);
        } catch (Throwable t) {
            response.setResult(t);
        }
        
        channel.write(response);
        return;
    }
    
    private ConcurrentMap<Class<?>, MethodAccess> methodAccessCache = Maps.newConcurrentMap();
    
}
