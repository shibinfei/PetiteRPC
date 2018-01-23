package io.hahahahaha.petiterpc.provider;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

/**
 * @author shibinfei
 *
 */
public enum ProviderContainer {

	INSTANCE;
	
    private ConcurrentMap<Class<?>, Object> providerCache = Maps.newConcurrentMap();
    
    public void register(Class<?> interfaceClass, Object provider) {
        providerCache.put(interfaceClass, provider);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        return (T)(providerCache.get(clazz));
    }
    
    public static ProviderContainer getInstance() {
    		return INSTANCE;
    }
    
}