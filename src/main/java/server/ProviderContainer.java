package server;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

/**
 * @author shibinfei
 *
 */
public class ProviderContainer {

    private ConcurrentMap<Class<?>, Object> providerCache = Maps.newConcurrentMap();
    
    public void register(Object provider) {
        providerCache.put(provider.getClass(), provider);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        return (T)(providerCache.get(clazz));
    }
    
}