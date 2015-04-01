package com.kenumir.materialsettings.items;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kenumir.materialsettings.MaterialSettingsItem;
import com.kenumir.materialsettings.R;

/**
 * Created by Kenumir on 2015-03-16.
 */
public class HeaderItem extends MaterialSettingsItem {

	private String title;

	public HeaderItem(Context ctx) {
		super(ctx, null);
	}

	@Override
	public int getViewResource() {
		return R.layout.item_header;
	}

	@Override
	public void setupView(View v) {
		((TextView) v).setText(title);
	}

	@Override
	public void save() {
		// NOP
	}

	public String getTitle() {
		return title;
	}

	public HeaderItem setTitle(String title) {
		this.title = title;
		return this;
	}
}
