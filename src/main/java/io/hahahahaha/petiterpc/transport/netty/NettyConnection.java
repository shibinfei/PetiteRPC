package io.hahahahaha.petiterpc.transport.netty;

import io.hahahahaha.petiterpc.transport.Connection;
import io.netty.channel.ChannelFuture;

/**
 * @author shibinfei
 *
 */
public class NettyConnection extends Connection {

    private ChannelFuture channelFuture;

    public NettyConnection(ChannelFuture channelFuture) {
        super();
        this.channelFuture = channelFuture;
    }
    
}
