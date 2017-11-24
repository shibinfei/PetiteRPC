package io.hahahahaha.petiterpc.serialization.protostuff;

import java.util.concurrent.ConcurrentHashMap;

import io.hahahahaha.petiterpc.serialization.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author shibinfei
 */
public class ProtostuffSerializer implements Serializer {

	private static ConcurrentHashMap<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	private <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
		
		if (schema != null) {
			return schema;
		}
		
		schema = RuntimeSchema.createFrom(clazz);
		schemaCache.put(clazz, schema);
		return schema;
	}
	
	@SuppressWarnings("unchecked")
	public <T> byte[] serialize(T object) {
		Schema<T> schema = getSchema((Class<T>)object.getClass());
		LinkedBuffer buffer = LinkedBuffer.allocate();
		byte[] bytes;
		
		try {
			bytes = ProtostuffIOUtil.toByteArray(object, schema, buffer);
		} finally {
			buffer.clear();
		}
		
		return bytes;
	}

	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		
		Schema<T> schema = getSchema(clazz);
		
		T object;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		ProtostuffIOUtil.mergeFrom(bytes, object, schema);
		return object;
	}

}
