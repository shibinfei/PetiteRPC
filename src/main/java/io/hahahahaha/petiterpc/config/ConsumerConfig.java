package io.hahahahaha.petiterpc.config;

import io.hahahahaha.petiterpc.registry.Registry;

/**
 * @author shibinfei
 */

public class ConsumerConfig {
	
	private Registry registry;

	public ConsumerConfig(Registry registry) {
		super();
		this.registry = registry;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	
}
