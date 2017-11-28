package io.hahahahaha.petiterpc.transport.netty;

import io.hahahahaha.petiterpc.transport.Connection;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author shibinfei
 *
 */
public class NettyConnection implements Connection {

    private ChannelFuture channelFuture;

    public NettyConnection(ChannelFuture channelFuture) {
        super();
        this.channelFuture = channelFuture;
    }

    @Override
    public void onConnect(Runnable cmd) {
        channelFuture.addListener(new ChannelFutureListener() {
            
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    cmd.run();
                }
            }
        });
    }
    
}
