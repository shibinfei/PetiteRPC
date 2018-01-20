package io.hahahahaha.petiterpc.transport.netty.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Consumer;

import io.hahahahaha.petiterpc.common.Address;
import io.hahahahaha.petiterpc.serialization.Serializer;
import io.hahahahaha.petiterpc.serialization.protostuff.ProtostuffSerializer;
import io.hahahahaha.petiterpc.transport.AddressChannelList;
import io.hahahahaha.petiterpc.transport.ChannelManager;
import io.hahahahaha.petiterpc.transport.Connection;
import io.hahahahaha.petiterpc.transport.Connector;
import io.hahahahaha.petiterpc.transport.TransportChannel;
import io.hahahahaha.petiterpc.transport.netty.Decoder;
import io.hahahahaha.petiterpc.transport.netty.Encoder;
import io.hahahahaha.petiterpc.transport.netty.NettyChannel;
import io.hahahahaha.petiterpc.transport.netty.NettyConnection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty客户端
 * @author shibinfei
 *
 */
public class NettyConnector implements Connector {

    private Serializer serializer = new ProtostuffSerializer();
    
	public NettyConnector() {
		
	}
	
	private Bootstrap bootstrap() {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		return bootstrap;
	}
	
	
	@Override
	public Connection connect(Address address, Consumer<TransportChannel> successEvent) {
		Bootstrap bootstrap = bootstrap();
		final SocketAddress socketAddress = InetSocketAddress.createUnresolved(address.getHost(), address.getPort());	
		
		AddressChannelList channelList = ChannelManager.getInstance().getByAddress(address);
		
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline()
				.addLast(new Encoder(serializer))
				.addLast(new Decoder(serializer))
				.addLast(new ClientHandler(channelList));
			}
			
		});
		
		ChannelFuture connectChannelFuture = bootstrap.connect(socketAddress);
		connectChannelFuture.addListener(new ChannelFutureListener() {
            
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                		TransportChannel transportChannel = NettyChannel.getInstance(future.channel());
                		successEvent.accept(transportChannel);
                }
            }
        });
		return new NettyConnection(connectChannelFuture);
	}

	@Override
	public void shutdownGracefully() {
		
	}

}