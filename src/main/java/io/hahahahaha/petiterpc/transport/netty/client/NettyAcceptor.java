package io.hahahahaha.petiterpc.transport.netty.client;

import io.netty.bootstrap.ServerBootstrap;

/**
 * @author shibinfei
 *
 */
public class NettyAcceptor implements Acceptor {

    private ServerBootstrap bootstrap;
    
    @Override
    public void bind(int port) {
        bootstrap.bind(port);
    }

    
    
}
