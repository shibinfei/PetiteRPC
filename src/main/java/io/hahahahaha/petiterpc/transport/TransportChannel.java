package io.hahahahaha.petiterpc.transport;

/**
 * @author shibinfei
 *
 */
public interface TransportChannel {

	/**
	 * 写消息
	 * @param msg
	 */
	void write(Object msg);
	
}