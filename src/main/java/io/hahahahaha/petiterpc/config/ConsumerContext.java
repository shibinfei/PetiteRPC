package io.hahahahaha.petiterpc.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import io.hahahahaha.petiterpc.loadbalancer.RoundRobinLoadBalancer;
import io.hahahahaha.petiterpc.proxy.JDKProxy;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.serialization.protostuff.ProtostuffSerializer;

/**
 * @author shibinfei
 */

public class ConsumerContext {

	private Registry registry;

	public ConsumerContext(Registry registry) {
		super();
		this.registry = registry;
	}
	
	/**
	 * 启动上下文. 所有操作都要在此之后执行
	 */
	public void start() {
		/*
		 * 从注册中心中获取服务提供者
		 */
		registry.connect();
	}
	
	/**
	 * 获取服务实例
	 * @param clazz
	 * @return
	 */
	public <T> T getService(Class<T> clazz) {
	    InvocationHandler invocationHandler = new JDKProxy(new RoundRobinLoadBalancer(), new ProtostuffSerializer());
	    Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, invocationHandler);
        return clazz.cast(object);
	}
	
}
