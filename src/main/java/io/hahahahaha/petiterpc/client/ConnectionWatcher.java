package io.hahahahaha.petiterpc.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import io.hahahahaha.petiterpc.common.Address;
import io.hahahahaha.petiterpc.registry.Registration;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.registry.RegistryListener;
import io.hahahahaha.petiterpc.transport.AddressChannelList;
import io.hahahahaha.petiterpc.transport.ChannelManager;
import io.hahahahaha.petiterpc.transport.Connection;
import io.hahahahaha.petiterpc.transport.Connector;

/**
 * @author shibinfei
 *
 */
public class ConnectionWatcher {

    private Class<?> interfaceClass; // 监听哪个服务

    private Registry registry;

    private Connector connector;

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private AtomicBoolean signalNeeded = new AtomicBoolean(true);

    private ChannelManager channelManager = ChannelManager.getInstance();

    public ConnectionWatcher(Class<?> interfaceClass, Registry registry) {
        super();
        this.interfaceClass = interfaceClass;
        this.registry = registry;
    }

    public void start() {
        establishConnection();
        waitConnection();
    }

    private void signal(boolean signalNeeded) {
        if (signalNeeded) {
            lock.lock();
            try {
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public void establishConnection() {
        registry.subscribe(interfaceClass, new RegistryListener() {

            @Override
            public void onRegistryOnline(Registration registration) {

                Address address = new Address(registration.getHost(), registration.getPort());
                AddressChannelList addressChannelList = channelManager.getByAddress(address);

                if (!addressChannelList.isEmpty()) {
                    channelManager.manage(interfaceClass, addressChannelList);
                    signal(signalNeeded.getAndSet(false));
                    return;
                }


                for (int i = 0; i < 5; i++) {

                    Runnable successEvent = () -> {
                        channelManager.manage(interfaceClass, addressChannelList);
                    };

                    Connection connection = connector.connect(address, successEvent);
                    
                }

                channelManager.manage(interfaceClass, addressChannelList);
                signal(signalNeeded.getAndSet(false));


                System.out.println("registryOnline" + registration.toString());
            }

            @Override
            public void onRegistryOffline(Registration registration) {
                System.out.println("offline");
            }

        });
    }


    public void waitConnection() {

        lock.lock();

        try {
            if (!channelManager.isServiceAvaiable(interfaceClass)) {
                condition.await(3, TimeUnit.SECONDS); // 写死, 等待时间3S.
                
                // 如果被notify, 但是还是没有服务, 抛出异常
                if (!channelManager.isServiceAvaiable(interfaceClass)) {
                    throw new RuntimeException("Wait time out for service [" + interfaceClass.getName() + "]");
                }

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
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
