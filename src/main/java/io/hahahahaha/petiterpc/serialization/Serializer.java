package io.hahahahaha.petiterpc.serialization;

/**
 * 序列化
 * @author shibinfei
 */
public interface Serializer {

	
	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	<T> byte[] serialize(T object);
	
	
	/**
	 * 反序列化
	 * @param bytes
	 * @param clazz
	 * @return
	 */
	<T> T deserialize(byte[] bytes, Class<T> clazz);
	
}