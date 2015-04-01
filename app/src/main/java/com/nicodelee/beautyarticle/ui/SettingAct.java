package com.nicodelee.beautyarticle.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.nicodelee.beautyarticle.viewhelper.SampleDialog;

/**
 * Created by Nicodelee on 15/3/31.
 */
public class SettingAct extends MaterialSettings implements SampleDialog.OnDialogOkClick {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addItem(new HeaderItem(this).setTitle("感谢有你"));
        addItem(new CheckboxItem(this, "key1").setTitle("无图模式").setSubtitle("更省流量").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                Toast.makeText(SettingAct.this, "CHECKED: " + isChecked, Toast.LENGTH_SHORT).show();
            }
        }));

        addItem(new DividerItem(this));
        addItem(new SwitcherItem(this, "key1a").setTitle("开启文章推送"));
        addItem(new DividerItem(this));
        addItem(new TextItem(this, "key6").setTitle("使用帮助").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem item) {
                SampleDialog.newInstance().show(getSupportFragmentManager(), "dialog1");
            }
        }));
        addItem(new DividerItem(this));
        addItem(new HeaderItem(this).setTitle("我是哪位？"));
        addItem(new TextItem(this, "key4").setTitle("关于作者").setSubtitle("感谢亲的使用").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                Toast.makeText(SettingAct.this, "Clicked 2", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public StorageInterface initStorageInterface() {
        return new PreferencesStorageInterface(this);
    }

    @Override
    public void onOkClick() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
