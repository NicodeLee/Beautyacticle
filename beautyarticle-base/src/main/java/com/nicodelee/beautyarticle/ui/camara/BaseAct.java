package com.nicodelee.beautyarticle.ui.camara;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.nicodelee.view.LoadingDialog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseAct extends AppCompatActivity {

  public LoadingDialog loadingDialog;
  public Intent intent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    intent = getIntent();
    loadingDialog = new LoadingDialog(this);
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public <T> T findViewById(View v, int id) {
    return (T) v.findViewById(id);
  }

  public <T> T findViewById(Activity activity, int id) {
    return (T) activity.findViewById(id);
  }

  @Override public void onStart() {
    EventBus.getDefault().register(this);
    super.onStart();
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe
  public void onEvent(Object event) {
  }

  protected boolean isStickyAvailable() {
    return false;
  }
}
