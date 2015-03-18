package com.nicodelee.utils;

/**
 * Description SharePreference存储工具类
 * 
 * Author lirizhi
 * Create date 2014-1-2
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	public static SPUtil spu;

	private SPUtil(Context context) {
		sp = context
				.getSharedPreferences("config_params", Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	// 单例
	public static SPUtil getsp(Context context) {
		if (spu == null)
			spu = new SPUtil(context);

		return spu;
	}

	/**
	 * 保存数据到sp
	 * 
	 * @param key
	 * @param value
	 */
	public void savePreference(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 从
	 * 
	 * @param key
	 * @return
	 */
	public String getPreference(String key) {
		return sp.getString(key, "");
	}

	/**
	 * 保存数据到sp
	 * 
	 * @param key
	 * @param value
	 */
	public void savebooleanPreference(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public boolean getbooleanPreference(String key) {
		return sp.getBoolean(key, false);
	}

}
