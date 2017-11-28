package io.hahahahaha.petiterpc.transport;

/**
 * @author shibinfei
 *
 */
public interface Connection {
    
    
    /**
     * 连接成功后的回调
     * @param cmd
     */
    void onConnect(Runnable cmd);

}