package server;

import io.hahahahaha.petiterpc.registry.Registry;

/**
 * @author shibinfei
 *
 */
public class ServerContext {

    private Registry registry;
    
    /**
     * 启动服务端上下文
     */
    public void start() {
        registry.connect();
    }
    
    
    
}
