package com.kenumir.materialsettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenumir.materialsettings.storage.StorageInterface;

/**
 * Created by Kenumir on 2015-03-16.
 */
public abstract class MaterialSettingsItem {

	protected Context mContext;
	protected MaterialSettings mMaterialSettings;
	protected String name;

	public MaterialSettingsItem(Context ctx, String name) {
		this.mContext = ctx;
		if (ctx instanceof MaterialSettings)
			this.mMaterialSettings = (MaterialSettings) ctx;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public View initView(ViewGroup parent, int res) {
		return LayoutInflater.from(mContext).inflate(res, parent, false);
	}

	public View getView(ViewGroup parent) {
		if (getViewResource() > 0) {
			View v = initView(parent, getViewResource());
			setupView(v);
			return v;
		} else
			return null;
	}

	public void setMaterialSettings(MaterialSettings m) {
		mMaterialSettings = m;
	}

	public StorageInterface getStorageInterface() {
		if (mMaterialSettings != null)
			return mMaterialSettings.getStorageInterface();
		else
			return null;
	}

	public abstract int getViewResource();
	public abstract void setupView(View v);
	public abstract void save();

}
