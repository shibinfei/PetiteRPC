package io.hahahahaha.petiterpc.registry;

/**
 * 事实上, 很多模型都和使用的开源软件统一. 比如Netty的Channel, ZK(非常典型)的Listener
 * @author shibinfei
 *
 */
public interface RegistryListener {

	/**
	 * 监听服务上线
	 * @param registration
	 */
	void onRegistryOnline(Registration registration);
	
	
	/**
	 * 监听服务下线
	 * @param registration
	 */
	void onRegistryOffline(Registration registration);
	
}