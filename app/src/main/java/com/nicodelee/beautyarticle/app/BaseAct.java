package com.nicodelee.beautyarticle.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.devspark.appmsg.AppMsg;
import com.nicodelee.beautyarticle.base.R;
import com.nicodelee.beautyarticle.internal.di.components.AppComponent;
import com.nicodelee.utils.WidgetController;
import com.nicodelee.view.LoadingDialog;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseAct extends AppCompatActivity {

  //public Navigator navigator;
  //@Inject Navigator navigator;
  public LoadingDialog loadingDialog;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResId());
    loadingDialog = new LoadingDialog(this);
    //RefWatcher refWatcher = APP.getRefWatcher(this);
    //refWatcher.watch(this);
    //navigator = getAppComponent().navigator();
    //this.getAppComponent().inject(this);
  }

  @Override public void onContentChanged() {//布局改变回调
    super.onContentChanged();
    ButterKnife.bind(this);
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void showShortToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void showInfo(String message) {
    AppMsg.Style style = new AppMsg.Style(1500, R.color.colorAccent);
    AppMsg appMsg = AppMsg.makeText(this, message, style);
    appMsg.setAnimation(R.anim.slide_in_top, R.anim.slide_out_top);
    appMsg.setLayoutGravity(Gravity.CENTER);
    appMsg.show();
  }

  public <T> T findViewById(View v, int id) {
    return (T) v.findViewById(id);
  }

  public <T> T findViewById(Activity activity, int id) {
    return (T) activity.findViewById(id);
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

  @Override public void onStart() {
    EventBus.getDefault().register(this);
    super.onStart();
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe public void onEvent(Object event) {
  }

  protected boolean isStickyAvailable() {
    return false;
  }

  abstract protected @LayoutRes int getLayoutResId();

  protected AppComponent getAppComponent() {
    return ((APP) getApplication()).getApplicationComponent();
  }
}
