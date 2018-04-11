package io.hahahahaha.petiterpc.common;

import java.util.Map;

import com.google.common.collect.Maps;

public class Config {

	private Map<String, String> configMap = Maps.newConcurrentMap();
	
	public Config set(String key, String value) {
		configMap.put(key, value);
		return this;
	}
	
	public String get(String key) {
		return configMap.get(key);
	}
	
}
