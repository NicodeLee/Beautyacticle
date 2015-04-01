package com.kenumir.materialsettings.items;

import android.content.Context;
import android.view.View;

import com.kenumir.materialsettings.MaterialSettingsItem;
import com.kenumir.materialsettings.R;

/**
 * Created by Kenumir on 2015-03-16.
 */
public class DividerItem extends MaterialSettingsItem {

	public DividerItem(Context ctx) {
		super(ctx, null);
	}

	@Override
	public int getViewResource() {
		return R.layout.item_divider;
	}

	@Override
	public void setupView(View v) {

	}

	@Override
	public void save() {
		// NOP
	}
}
