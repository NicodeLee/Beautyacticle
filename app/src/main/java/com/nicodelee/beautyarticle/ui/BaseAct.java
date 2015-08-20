package com.nicodelee.beautyarticle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.view.LoadingDialog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.greenrobot.event.EventBus;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseAct extends AppCompatActivity {

  public LoadingDialog loadingDialog;
  public Intent intent;
  private static BaseAct Cot;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Cot = this;
    intent = getIntent();
    loadingDialog = new LoadingDialog(this);
  }

  public <T> T findViewByIdExt(int id) {
    return (T) super.findViewById(id);
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public APP getApp() {
    return (APP) getApplication();
  }

  public void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void showShortToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void showInfo(String message) {
    AppMsg.Style style = new AppMsg.Style(1500, R.color.accent);
    AppMsg appMsg = AppMsg.makeText(this, message, style);
    appMsg.setAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    appMsg.setLayoutGravity(Gravity.CENTER);
    appMsg.show();
  }

  public void skipIntent(Class clz, HashMap<String, Object> map, boolean isFinish) {
    Intent intent = new Intent(this, clz);
    if (map != null) {
      Iterator it = map.entrySet().iterator();

      while (it.hasNext()) {

        Map.Entry entry = (Map.Entry) it.next();

        String key = (String) entry.getKey();

        Serializable value = (Serializable) entry.getValue();

        intent.putExtra(key, value);
      }
    }
    startActivity(intent);
    if (isFinish) finish();
  }

  public void skipIntent(Class clz, HashMap<String, Object> map, int code) {
    Intent intent = new Intent(this, clz);
    if (map != null) {
      Iterator it = map.entrySet().iterator();

      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        String key = (String) entry.getKey();
        Serializable value = (Serializable) entry.getValue();
        intent.putExtra(key, value);
      }
    }
    startActivityForResult(intent, code);
  }

  public void skipIntent(Class clz, int code, boolean isFinish) {
    Intent intent = new Intent(this, clz);
    startActivityForResult(intent, code);
    if (isFinish) finish();
  }

  public void skipIntent(Class clz, boolean isFinish) {
    Intent intent = new Intent(this, clz);
    startActivity(intent);
    if (isFinish) finish();
  }

  public Object getExtra(String name) {
    return getIntent().getSerializableExtra(name);
  }

  @Override protected void onStart() {
    EventBus.getDefault().registerSticky(this);
    super.onStart();
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  public void onEvent(int event) {
  }
}
