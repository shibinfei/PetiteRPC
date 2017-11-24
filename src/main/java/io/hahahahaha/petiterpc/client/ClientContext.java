package io.hahahahaha.petiterpc.client;

import java.lang.reflect.Proxy;
import java.util.List;

import io.hahahahaha.petiterpc.loadbalancer.LoadBalancer;
import io.hahahahaha.petiterpc.loadbalancer.RoundRobinLoadBalancer;
import io.hahahahaha.petiterpc.proxy.JDKProxy;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.serialization.Serializer;
import io.hahahahaha.petiterpc.serialization.protostuff.ProtostuffSerializer;

/**
 * @author shibinfei
 *
 */
public class ClientContext {

	private Registry registry;
	
	private List<Class<?>> interfaces;
	
	public void start() {
		registry.connect();
		
		for (Class<?> clazz : interfaces) {
			ConnectionWatcher connectionWatcher = new ConnectionWatcher(clazz, registry);
			connectionWatcher.start();
		}
	}
	
	
	public <T> T getService(Class<T> clazz) {
	    Serializer serializer = new ProtostuffSerializer();
	    LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
	    JDKProxy proxy = new JDKProxy(loadBalancer, serializer);
	    Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, proxy);
        return clazz.cast(object);
	}
	
	
}
