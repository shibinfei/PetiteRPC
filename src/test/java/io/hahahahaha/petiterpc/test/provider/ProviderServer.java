package io.hahahahaha.petiterpc.test.provider;

import java.util.concurrent.locks.LockSupport;

import io.hahahahaha.petiterpc.provider.ProviderContext;
import io.hahahahaha.petiterpc.registry.zookeeper.ZookeeperRegistry;
import io.hahahahaha.petiterpc.transport.netty.provider.NettyAcceptor;

public class ProviderServer {

	public static void main(String[] args) throws InterruptedException {
		ProviderContext context = new ProviderContext();
		context.setBasePackage("io.hahahahaha");	// 扫描此包下标记有@Provider注解的服务 
		context.setAcceptor(new NettyAcceptor());	// 使用netty
		context.setPort(8765);	// 监听端口
		context.setRegistry(new ZookeeperRegistry("10.1.5.101:2181"));	// 注册中心
		context.start();
		LockSupport.park();
	}
	
}