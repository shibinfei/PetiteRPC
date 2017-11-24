package io.hahahahaha.petiterpc.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import io.hahahahaha.petiterpc.registry.Registration;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.registry.RegistryListener;
import io.hahahahaha.petiterpc.transport.netty.client.Connector;

/**
 * @author shibinfei
 *
 */
public class ConnectionWatcher {

	private Class<?> interfaceClass;	// 监听哪个服务
	
	private Registry registry;
	
	private Connector connector;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
	
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
				
			}

			@Override
			public void onRegistryOffline(Registration registration) {
				
			}
			
		});
	}
	
	
	public void waitConnection() {
		
		lock.lock();
		
		try {
			if (!connector.isServiceAvaiable(interfaceClass)) {
				condition.await(3, TimeUnit.SECONDS);	// 写死, 等待时间3S. 
				
				// 如果被notify, 但是还是没有服务, 抛出异常
				if (!connector.isServiceAvaiable(interfaceClass)) {
					throw new RuntimeException("Wait time out for service [" + interfaceClass.getName() + "]");
				}
				
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
		
	}
	
}
