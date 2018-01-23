package io.hahahahaha.petiterpc.transport.netty.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.consumer.ResponseTask;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端Handler
 * @author shibinfei
 *
 */
@ChannelHandler.Sharable
public class ConsumerHandler extends ChannelInboundHandlerAdapter {

    private static ExecutorService responseExecutor = Executors.newFixedThreadPool(4);
    
    public ConsumerHandler() {
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        responseExecutor.execute(new ResponseTask(response));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        channel.close();
    }

}
