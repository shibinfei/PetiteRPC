package io.hahahahaha.petiterpc.test.consumer;

import java.util.Arrays;

import io.hahahahaha.petiterpc.consumer.ConsumerContext;
import io.hahahahaha.petiterpc.registry.zookeeper.ZookeeperRegistry;
import io.hahahahaha.petiterpc.test.provider.DemoInterface;

public class ConsumerDemo {

	public static void main(String[] args) {
		ConsumerContext context = new ConsumerContext();
		context.setInterfaces(Arrays.asList(DemoInterface.class));
		context.setRegistry(new ZookeeperRegistry("10.1.5.101:2181"));
		context.start();
		
		DemoInterface demo = context.getService(DemoInterface.class);
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				String result = demo.fuck("hahah");
				System.out.println(result);
			}).start();
		}
		
	}
	
}
