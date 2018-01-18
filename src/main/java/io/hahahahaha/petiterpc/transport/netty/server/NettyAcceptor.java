package io.hahahahaha.petiterpc.transport.netty.server;

import java.net.InetSocketAddress;

import io.hahahahaha.petiterpc.serialization.Serializer;
import io.hahahahaha.petiterpc.serialization.protostuff.ProtostuffSerializer;
import io.hahahahaha.petiterpc.transport.Acceptor;
import io.hahahahaha.petiterpc.transport.netty.Decoder;
import io.hahahahaha.petiterpc.transport.netty.Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author shibinfei
 *
 */
public class NettyAcceptor implements Acceptor {

    private Serializer serializer = new ProtostuffSerializer();
    
    @Override
    public void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(8888))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerHandler(), new Encoder(serializer), new Decoder(serializer));
                    }
                    
                });
        
        bootstrap.bind(port);
    }

    
    
}
