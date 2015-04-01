package com.kenumir.materialsettings.storage;

import java.util.Map;

/**
 * Created by Kenumir on 2015-03-16.
 */
public abstract class StorageInterface {

	public StorageInterface() {}

	public abstract void save(String key, Boolean value);
	public abstract boolean load(String key, Boolean defaultValue);

	public abstract void save(String key, String value);
	public abstract String load(String key, String defaultValue);

	public abstract void save(String key, Integer value);
	public abstract Integer load(String key, Integer defaultValue);

	public abstract void save(String key, Float value);
	public abstract Float load(String key, Float defaultValue);

	public abstract void save(String key, Long value);
	public abstract Long load(String key, Long defaultValue);

	public abstract Map<String, ?> getAll();

}
