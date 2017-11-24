package io.hahahahaha.petiterpc.registry;

/**
 * @author shibinfei
 */

public interface Registry {

	/**
	 * 连接注册服务
	 */
	void connect();
	
	
	/**
	 * 注册服务
	 * @param registration
	 */
	void register(Registration registration);
	
	
	/**
	 * 取消注册
	 * @param registration
	 */
	void unregister(Registration registration);
	
	
	/**
	 * 订阅 (当服务提供者变化时得到通知)
	 * @param serviceName
	 */
	void subscribe(Class<?> interfaceClass, RegistryListener registryListener);
	
	
	/**
	 * 关闭
	 */
	void shutdownGracefully();
}