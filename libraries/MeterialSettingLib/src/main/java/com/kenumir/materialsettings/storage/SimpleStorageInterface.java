package com.kenumir.materialsettings.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kenumir on 2015-03-16.
 */
public class SimpleStorageInterface extends StorageInterface {

	private HashMap<String, Object> mem = new HashMap<>();

	public SimpleStorageInterface() {

	}

	@Override
	public Map<String, ?> getAll() {
		return mem;
	}

	@Override
	public void save(String key, Boolean value) {
		mem.put(key, Boolean.valueOf(value));
	}

	@Override
	public boolean load(String key, Boolean defaultValue) {
		if (mem.containsKey(key))
			return (boolean) mem.get(key);
		return defaultValue;
	}

	@Override
	public void save(String key, String value) {
		mem.put(key, String.valueOf(value));
	}

	@Override
	public String load(String key, String defaultValue) {
		if (mem.containsKey(key))
			return (String) mem.get(key);
		return defaultValue;
	}

	@Override
	public void save(String key, Integer value) {
		mem.put(key, Integer.valueOf(value));
	}

	@Override
	public Integer load(String key, Integer defaultValue) {
		if (mem.containsKey(key))
			return (Integer) mem.get(key);
		return defaultValue;
	}

	@Override
	public void save(String key, Float value) {
		mem.put(key, Float.valueOf(value));
	}

	@Override
	public Float load(String key, Float defaultValue) {
		if (mem.containsKey(key))
			return (Float) mem.get(key);
		return defaultValue;
	}

	@Override
	public void save(String key, Long value) {
		mem.put(key, Long.valueOf(value));
	}

	@Override
	public Long load(String key, Long defaultValue) {
		if (mem.containsKey(key))
			return (Long) mem.get(key);
		return defaultValue;
	}

}
