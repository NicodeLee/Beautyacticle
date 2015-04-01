package com.kenumir.materialsettings.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Kenumir on 2015-03-18.
 */
public class PreferencesStorageInterface extends StorageInterface  {

	private SharedPreferences prefs;

	public PreferencesStorageInterface(Context ctx) {
		prefs = ctx.getSharedPreferences(ctx.getPackageName() + "_materialsettings", Context.MODE_PRIVATE);
	}

	@Override
	public void save(String key, Boolean value) {
		prefs.edit().putBoolean(key, value).apply();
	}

	@Override
	public boolean load(String key, Boolean defaultValue) {
		return prefs.getBoolean(key, defaultValue);
	}

	@Override
	public void save(String key, String value) {
		prefs.edit().putString(key, value).apply();
	}

	@Override
	public String load(String key, String defaultValue) {
		return prefs.getString(key, defaultValue);
	}

	@Override
	public void save(String key, Integer value) {
		prefs.edit().putInt(key, value).apply();
	}

	@Override
	public Integer load(String key, Integer defaultValue) {
		return prefs.getInt(key, defaultValue);
	}

	@Override
	public void save(String key, Float value) {
		prefs.edit().putFloat(key, value).apply();
	}

	@Override
	public Float load(String key, Float defaultValue) {
		return prefs.getFloat(key, defaultValue);
	}

	@Override
	public void save(String key, Long value) {
		prefs.edit().putLong(key, value).apply();
	}

	@Override
	public Long load(String key, Long defaultValue) {
		return prefs.getLong(key, defaultValue);
	}

	@Override
	public Map<String, ?> getAll() {

		return prefs.getAll();
	}
}
