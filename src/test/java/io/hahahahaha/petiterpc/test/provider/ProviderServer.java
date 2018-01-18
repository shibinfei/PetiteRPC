package io.hahahahaha.petiterpc.test.provider;

import java.util.concurrent.CountDownLatch;

import io.hahahahaha.petiterpc.registry.zookeeper.ZookeeperRegistry;
import io.hahahahaha.petiterpc.server.ServerContext;
import io.hahahahaha.petiterpc.transport.netty.server.NettyAcceptor;

public class ProviderServer {

	public static void main(String[] args) throws InterruptedException {
		ServerContext context = new ServerContext();
		context.setBasePackage("io.hahahahaha");
		context.setAcceptor(new NettyAcceptor());
		context.setPort(8765);
		context.setRegistry(new ZookeeperRegistry("10.1.12.107:2181"));
		context.start();
		new CountDownLatch(1).await();
	}
	
}
