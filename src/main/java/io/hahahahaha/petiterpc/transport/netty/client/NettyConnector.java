package io.hahahahaha.petiterpc.transport.netty.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import io.hahahahaha.petiterpc.serialization.Serializer;
import io.hahahahaha.petiterpc.transport.Connection;
import io.hahahahaha.petiterpc.transport.Connector;
import io.hahahahaha.petiterpc.transport.netty.Decoder;
import io.hahahahaha.petiterpc.transport.netty.Encoder;
import io.hahahahaha.petiterpc.transport.netty.NettyConnection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

    private Serializer serializer;
    
	public NettyConnector() {
		
	}
	
	private Bootstrap bootstrap() {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup).channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		return bootstrap;
	}
	
	
	@Override
	public Connection connect(String host, int port) {
		Bootstrap bootstrap = bootstrap();
		final SocketAddress socketAddress = InetSocketAddress.createUnresolved(host, port);	
		
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline()
				.addLast(new ClientHandler())
				.addLast(new Encoder(serializer))
				.addLast(new Decoder(serializer));
			}
			
		});
		
		ChannelFuture connectChannelFuture = bootstrap.connect(socketAddress);
		return new NettyConnection(connectChannelFuture);
	}

	@Override
	public void shutdownGracefully() {
		
	}

	@Override
	public boolean isServiceAvaiable(Class<?> interfaceName) {
		
	    
	    
		return false;
	}

}