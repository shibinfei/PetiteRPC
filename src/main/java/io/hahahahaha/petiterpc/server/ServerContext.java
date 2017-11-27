package io.hahahahaha.petiterpc.server;

import java.io.IOException;
import java.util.Set;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import io.hahahahaha.petiterpc.common.Provider;
import io.hahahahaha.petiterpc.common.utils.NetUtils;
import io.hahahahaha.petiterpc.registry.Registration;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.transport.Acceptor;

/**
 * @author shibinfei
 *
 */
public class ServerContext {

    private Registry registry;
    
    private Acceptor acceptor;
    
    private ProviderContainer providerContainer = new ProviderContainer();
    
    private String basePackage;
    
    private int port;
    
    private static final Objenesis objenesis = new ObjenesisStd(true);
    
    /**
     * 启动服务端上下文
     */
    public void start() {
        registry.connect();
        initProviders();
        acceptor.bind(port);
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
            
            Class<?> clazz = classInfo.load();
            if (clazz.getAnnotation(Provider.class) == null) {
                continue;
            }
            
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces == null || interfaces.length == 0) {
                throw new RuntimeException("Must implement interface");
            }
            
            Object providerInstance = objenesis.newInstance(clazz);
            providerContainer.register(interfaces[0], providerInstance);
            registry.register(new Registration(NetUtils.getLocalAddress(), port, interfaces[0].getName()));
        }
        
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    public ProviderContainer getProviderContainer() {
        return providerContainer;
    }

    public void setProviderContainer(ProviderContainer providerContainer) {
        this.providerContainer = providerContainer;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
}
