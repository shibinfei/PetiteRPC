package io.hahahahaha.petiterpc.registry.zookeeper;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.hahahahaha.petiterpc.registry.Registration;
import io.hahahahaha.petiterpc.registry.Registry;
import io.hahahahaha.petiterpc.registry.RegistryListener;

/**
 * Zookeeper注册服务
 * 
 * <p>
 * zk路径如下{@code /petiterpc/${interfaceName}/${ip}:${port}/}
 * </p>
 * 
 * @author shibinfei
 */

public class ZookeeperRegistry implements Registry {

	private static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

	public ZookeeperRegistry(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	private CuratorFramework client;

	private String serverUrl;

	// 缓存所有的ZK节点信息
	private static final ConcurrentMap<String, PathChildrenCache> services = Maps.newConcurrentMap();
	
	// 当前节点 注意, 当前节点所有注册的服务
	private static final Set<Registration> registrations = Sets.newConcurrentHashSet();

	@Override
	public void connect() {

		logger.info("Connecting Zookeeper Server {}", serverUrl);

		client = CuratorFrameworkFactory.newClient(serverUrl, 5000, 5000, new ExponentialBackoffRetry(500, 20));
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {

			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				logger.info("Zookeeper state changed {}", newState);

				if (newState == ConnectionState.RECONNECTED) {
					// TODO
				}
			}
		});

		client.start();
	}

	@Override
	public void register(Registration registration) {
		
		/*
		 * Provider将自己注册到注册中心
		 * 1. 目录写到Zookeeper
		 */
		
		String path = toServiceDir(registration.getInterfaceName());

		// 创建服务目录
		try {
			if (client.checkExists().forPath(path) == null) {
				client.create().creatingParentsIfNeeded().forPath(path);
			}

			// 指定服务的地址
			client.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {

				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					logger.info("Event {}", event);
					registrations.add(registration);
				}
			}).forPath(toRegistrationPath(registration));
		} catch (Exception e) {
			logger.error("Fail to register", e);
		}

	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	@Override
	public void unregister(Registration registration) {
		String dirPath = toServiceDir(registration.getInterfaceName());

		try {
			if (client.checkExists().forPath(dirPath) == null) {
				return;
			}

			client.delete().inBackground(new BackgroundCallback() {

				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					registrations.remove(registration);
				}
			}).forPath(toRegistrationPath(registration));

		} catch (Exception e) {
			logger.error("Fail to unregister", e);
		}

	}


	@Override
	public void subscribe(Class<?> interfaceClass, RegistryListener registryListener) {

		/*
		 * 下面这段代码只是做了缓存. 噪音
		 */

		String serviceDirectory = toServiceDir(interfaceClass.getName());
		PathChildrenCache pathChildrenCache = services.get(serviceDirectory);

		if (pathChildrenCache != null) {
			return;
		}

		PathChildrenCache newPathChildrenCache = new PathChildrenCache(client, serviceDirectory, false);
		pathChildrenCache = services.putIfAbsent(serviceDirectory, newPathChildrenCache);

		if (pathChildrenCache != null) {
			return;
		}

		/*
		 * 噪音结束
		 */

		newPathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				// 解析注册的服务地址
				String servicePath = event.getData().getPath();
				Registration registration = fromPath(servicePath);

				// 刚刚注册时, zk会将已有的节点都推送一遍.
				if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
					registryListener.onRegistryOnline(registration);
					return;
				}

				if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
					registryListener.onRegistryOffline(registration);
					return;
				}

			}
		});

		try {
			newPathChildrenCache.start();
		} catch(Exception e) {
			logger.error("T", e);
		}
		
	}

	private static final String basePath = "/petiterpc/";

	private static Registration fromPath(String path) {
		String subPath = StringUtils.substringAfter(path, basePath);
		String[] array = subPath.split("/");
		String serviceName = array[0];
		String[] address = array[1].split(":");
		Registration registration = new Registration(address[0], Integer.parseInt(address[1]), serviceName);
		return registration;
	}

	private static String toServiceDir(String interfaceName) {
		return String.format(basePath + "%s", interfaceName);
	}

	private static String toRegistrationPath(Registration registration) {
		return toServiceDir(registration.getInterfaceName())
				+ String.format("/%s:%s", registration.getHost(), registration.getPort());
	}

	@Override
	public void shutdownGracefully() {
		
        for (PathChildrenCache childrenCache : services.values()) {
            try {
                childrenCache.close();
            } catch (IOException ignored) {}
        }
		
		client.close();
	
	}
	
}
