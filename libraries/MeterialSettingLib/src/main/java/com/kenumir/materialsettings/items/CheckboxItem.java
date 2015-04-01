package com.kenumir.materialsettings.items;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kenumir.materialsettings.MaterialSettingsItem;
import com.kenumir.materialsettings.R;
import com.kenumir.materialsettings.views.CheckableLinearLayout;

/**
 * Created by Kenumir on 2015-03-16.
 */
public class CheckboxItem extends MaterialSettingsItem {

	public static interface OnCheckedChangeListener {
		public void onCheckedChange(CheckboxItem item, boolean isChecked);
	}

	private String title, subtitle;
	private boolean checked, defaultValue = false;
	private TextView titleView, subtitleView;
	private CheckableLinearLayout mCheckableLinearLayout;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	public CheckboxItem(Context ctx, String name) {
		super(ctx, name);
	}

	@Override
	public int getViewResource() {
		return R.layout.item_checkbox;
	}

	@Override
	public void setupView(View v) {

		checked = getStorageInterface() != null ? getStorageInterface().load(name, isDefaultValue()) : isDefaultValue();
		mCheckableLinearLayout = (CheckableLinearLayout) v;
		titleView = (TextView) v.findViewById(R.id.material_dialog_item_title);
		subtitleView = (TextView) v.findViewById(R.id.material_dialog_item_subtitle);

		updateChecked(checked);
		updateTitle(title);
		updateSubTitle(subtitle);

		mCheckableLinearLayout.setChecked(checked);
		mCheckableLinearLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				save();
				if (getOnCheckedChangeListener() != null)
					getOnCheckedChangeListener().onCheckedChange(CheckboxItem.this, isChecked);
			}
		});
	}

	@Override
	public void save() {
		if (getStorageInterface() != null)
			getStorageInterface().save(name, isChecked());
	}

	public CheckboxItem updateTitle(String newTitle) {
		if (titleView != null)
			titleView.setText(newTitle);
		return this;
	}

	public CheckboxItem updateSubTitle(String newSubTitle) {
		if (subtitleView != null) {
			subtitleView.setText(newSubTitle);
			subtitleView.setVisibility(subtitle != null && subtitle.trim().length() > 0 ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public String getTitle() {
		return title;
	}

	public CheckboxItem setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public CheckboxItem setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public boolean isChecked() {
		//Log.d("tests", "C: " + name + "=" + mCheckableLinearLayout.isChecked());
		return mCheckableLinearLayout.isChecked();
	}

	public CheckboxItem updateChecked(boolean val) {
		mCheckableLinearLayout.setChecked(val);
		return this;
	}

	public OnCheckedChangeListener getOnCheckedChangeListener() {
		return mOnCheckedChangeListener;
	}

	public CheckboxItem setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
		this.mOnCheckedChangeListener = mOnCheckedChangeListener;
		return this;
	}

	public boolean isDefaultValue() {
		return defaultValue;
	}

	public CheckboxItem setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

}
