package io.hahahahaha.petiterpc.transport.netty.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.server.ServerMediator;
import io.hahahahaha.petiterpc.transport.TransportChannel;
import io.hahahahaha.petiterpc.transport.netty.NettyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author shibinfei
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ServerMediator serverMediator = new ServerMediator();
    
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
				serverMediator.handleRequest(transportChannel, request);
			}
		});
    }
    
}
