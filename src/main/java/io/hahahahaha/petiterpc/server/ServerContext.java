package io.hahahahaha.petiterpc.server;

import java.io.IOException;
import java.util.Set;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.transport.Acceptor;

/**
 * @author shibinfei
 *
 */
public class ServerContext {

    private Registry registry;
    
    private Acceptor acceptor;
    
    private ProviderContainer providerContainer;
    
    private String basePackage;
    
    private static final Objenesis objenesis = new ObjenesisStd(true);
    
    /**
     * 启动服务端上下文
     */
    public void start() {
        acceptor.bind(8888);
        registry.connect();
        initProviders();
    }
    
    private void initProviders() {
        ClassPath classPath;
        try {
            classPath = ClassPath.from(ServerContext.class.getClassLoader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Set<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(basePackage);
        
        for (ClassInfo classInfo : classInfos) {
            Class<?> clazz = classInfo.getClass();
            
            Class<?> superclass = clazz.getSuperclass();
            if (!superclass.isInterface()) {
                throw new RuntimeException("Must implement interface");
            }
            providerContainer.register(superclass, objenesis.newInstance(clazz));
        }
        
    }
    
    
}
