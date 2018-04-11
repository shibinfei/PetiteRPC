package io.hahahahaha.petiterpc.transport.netty.consumer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.hahahahaha.petiterpc.common.Heartbeat;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class HeartbeatHandler extends ChannelDuplexHandler {

	private volatile long lastReadNanos;
	
	private volatile long lastWriteNanos;
	
	public HeartbeatHandler() {
		
	}
	
	private void initialize(ChannelHandlerContext ctx) {
		Runnable heartbeatTask = () -> {
			
			long latestIO = Math.max(lastWriteNanos, lastReadNanos);
			
			// 太久没有读写, 发心跳
			if (System.nanoTime() - latestIO > TimeUnit.MINUTES.toNanos(1)) {
				Heartbeat heartbeat = new Heartbeat();
				heartbeat.setRequest(true);
				ctx.channel().write(heartbeat);
			}
			
			// reconnect
			// 关闭连接
			
			
		};
		
		Executors.newSingleThreadScheduledExecutor()
			.scheduleAtFixedRate(heartbeatTask, 1, 1, TimeUnit.MINUTES);
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		initialize(ctx);
		lastReadNanos = lastWriteNanos = System.nanoTime();
		ctx.fireChannelActive();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		lastReadNanos = lastWriteNanos = 0;
		ctx.fireChannelInactive();
	}
	

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		if (msg instanceof Heartbeat) {
			Heartbeat heartbeat = (Heartbeat)msg;
			if (heartbeat.isRequest()) {
				ctx.channel().write(new Heartbeat());
			}
			return;
		}
		
		lastReadNanos = System.nanoTime();
		ctx.fireChannelRead(msg);
	}
	
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                lastWriteNanos = System.nanoTime();
            }
        });
		ctx.write(msg, promise);
	}
	
}
