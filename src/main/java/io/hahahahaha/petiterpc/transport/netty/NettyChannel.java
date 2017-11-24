package io.hahahahaha.petiterpc.transport.netty;

import java.util.Map;

import com.google.common.collect.Maps;

import io.hahahahaha.petiterpc.transport.TransportChannel;
import io.netty.channel.Channel;

/**
 * @author shibinfei
 *
 */
public class NettyChannel implements TransportChannel {

	private Channel channel;
	
	private static Map<Channel, NettyChannel> channelCache = Maps.newConcurrentMap();

	public static NettyChannel getInstance(Channel channel) {
	    return channelCache.computeIfAbsent(channel, key -> new NettyChannel());
	}
	
	@Override
	public void write(Object msg) {
		channel.write(msg);
	}
	
}
