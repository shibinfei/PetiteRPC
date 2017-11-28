package io.hahahahaha.petiterpc.transport;

import io.hahahahaha.petiterpc.common.Address;

/**
 * @author shibinfei
 *
 */
public interface Connector {

	/**
	 * 连接远程节点
	 * @param ip
	 * @param port
	 * @return
	 */
	Connection connect(Address address, Runnable successEvent);
	

	boolean isServiceAvaiable(Class<?> interfaceName);
	
	
	void shutdownGracefully();
}
