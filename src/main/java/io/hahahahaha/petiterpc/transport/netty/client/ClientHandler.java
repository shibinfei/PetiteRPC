package io.hahahahaha.petiterpc.transport.netty.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.hahahahaha.petiterpc.client.ResponseTask;
import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.transport.AddressChannelList;
import io.hahahahaha.petiterpc.transport.TransportChannel;
import io.hahahahaha.petiterpc.transport.netty.NettyChannel;
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
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static ExecutorService responseExecutor = Executors.newFixedThreadPool(4);
    
    public ClientHandler(AddressChannelList addressChannelList) {
        this.addressChannelList = addressChannelList;
    }
    
    private AddressChannelList addressChannelList;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        Channel channel = ctx.channel();
        TransportChannel transportChannel = NettyChannel.getInstance(channel);
        responseExecutor.execute(new ResponseTask(transportChannel, response));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        channel.close();
    }

}
