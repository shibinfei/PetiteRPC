package io.hahahahaha.petiterpc.transport.netty.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.provider.ProviderLimitTask;
import io.hahahahaha.petiterpc.transport.TransportChannel;
import io.hahahahaha.petiterpc.transport.netty.NettyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author shibinfei
 *
 */
public class ProviderHandler extends ChannelInboundHandlerAdapter {

    private ExecutorService threadPool = Executors.newFixedThreadPool(4);
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        if (!(msg instanceof Request)) {
           return;
        }
        
        Request request = (Request) msg;
        TransportChannel transportChannel = NettyChannel.getInstance(ctx.channel());
        
        threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				new ProviderLimitTask(transportChannel, request).execute();
			}
		});
    }
    
}
