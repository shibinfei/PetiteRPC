package io.hahahahaha.petiterpc.transport.netty;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	private NettyChannel(Channel channel) {
	    this.channel = channel;
	}
	
	public static NettyChannel getInstance(Channel channel) {
	    return channelCache.computeIfAbsent(channel, key -> new NettyChannel(channel));
	}
	
	@Override
	public void write(Object msg) {
		channel.write(msg);
	}
	
	@Override
	public int hashCode() {
	    return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public boolean equals(Object that) {
	    return EqualsBuilder.reflectionEquals(this, that);
	}

    @Override
    public String toString() {
        return "NettyChannel [channel=" + channel + "]";
    }
	
}
