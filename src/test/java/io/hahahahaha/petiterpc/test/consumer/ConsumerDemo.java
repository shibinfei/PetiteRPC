package io.hahahahaha.petiterpc.test.consumer;

import java.util.Arrays;

import io.hahahahaha.petiterpc.client.ClientContext;
import io.hahahahaha.petiterpc.registry.zookeeper.ZookeeperRegistry;
import io.hahahahaha.petiterpc.test.provider.DemoInterface;

public class ConsumerDemo {

	public static void main(String[] args) {
		ClientContext context = new ClientContext();
		context.setInterfaces(Arrays.asList(DemoInterface.class));
		context.setRegistry(new ZookeeperRegistry("10.1.12.107:2181"));
		context.start();
		
		DemoInterface demo = context.getService(DemoInterface.class);
		String result = demo.fuck("hahah");
		System.out.println(result);
	}
	
}
