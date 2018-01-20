package io.hahahahaha.petiterpc.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.hahahahaha.petiterpc.common.Address;
import io.hahahahaha.petiterpc.registry.Registration;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.registry.RegistryListener;
import io.hahahahaha.petiterpc.transport.AddressChannelList;
import io.hahahahaha.petiterpc.transport.ChannelManager;
import io.hahahahaha.petiterpc.transport.Connector;
import io.hahahahaha.petiterpc.transport.TransportChannel;

/**
 * @author shibinfei
 *
 */
public class ConnectionWatcher {

	private Class<?> interfaceClass; // 监听哪个服务

	private Registry registry;

	private Connector connector;

	private ChannelManager channelManager = ChannelManager.getInstance();

	private CountDownLatch countDownLatch = new CountDownLatch(1);	// 多次release之后, 只不过后面的会返回false而已. 
	
	public ConnectionWatcher(Class<?> interfaceClass, Registry registry) {
		super();
		this.interfaceClass = interfaceClass;
		this.registry = registry;
	}

	public void start() {
		establishConnection();
		waitConnection();
	}

	public void establishConnection() {
		registry.subscribe(interfaceClass, new RegistryListener() {

			@Override
			public void onRegistryOnline(Registration registration) {

				Address address = new Address(registration.getHost(), registration.getPort());
				AddressChannelList addressChannelList = channelManager.getByAddress(address);

				if (!addressChannelList.isEmpty()) {
					channelManager.manage(interfaceClass, addressChannelList);
					countDownLatch.countDown();
					return;
				}

				for (int i = 0; i < 5; i++) {

					Consumer<TransportChannel> successEvent = transportChannel -> {
						addressChannelList.add(transportChannel);
						channelManager.manage(interfaceClass, addressChannelList);
						countDownLatch.countDown();
					};

					connector.connect(address, successEvent);	// Connection connection = 
				}

				System.out.println("registryOnline" + registration.toString());
			}

			@Override
			public void onRegistryOffline(Registration registration) {
				System.out.println("offline");
			}

		});
	}

	public void waitConnection() {

		if (!channelManager.isServiceAvaiable(interfaceClass)) {

			try {
				countDownLatch.await(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			// 如果被notify, 但是还是没有服务, 抛出异常
			if (!channelManager.isServiceAvaiable(interfaceClass)) {
				throw new RuntimeException("Wait time out for service [" + interfaceClass.getName() + "]");
			}

		}

	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

}
