package com.nicodelee.beautyarticle.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.devspark.appmsg.AppMsg;
import com.nicodelee.beautyarticle.internal.di.components.ApiComponent;
import com.nicodelee.beautyarticle.internal.di.components.AppComponent;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import nucleus.presenter.Presenter;
import nucleus.view.NucleusSupportFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseSupportFragment<PresenterType extends Presenter>
    extends NucleusSupportFragment<PresenterType> {

  private Context context;

  @Override public void onCreate(Bundle savedInstanceState) {
    injectorPresenter();
    super.onCreate(savedInstanceState);
    context = getActivity().getApplicationContext();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //Icepick.restoreInstanceState(this, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    //Icepick.saveInstanceState(this, bundle);
  }

  // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
  @Override public void onDetach() {
    super.onDetach();
    try {
      Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
      childFragmentManager.setAccessible(true);
      childFragmentManager.set(this, null);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public FragmentActivity mActivity;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = (FragmentActivity) activity;
  }

  public APP getApp() {
    return (APP) getActivity().getApplication();
  }

  public void showToast(String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  public void showInfo(String message) {
    AppMsg.Style style =
        new AppMsg.Style(1500, com.nicodelee.beautyarticle.base.R.color.colorAccent);
    AppMsg appMsg = AppMsg.makeText(getActivity(), message, style);
    appMsg.setAnimation(com.nicodelee.beautyarticle.base.R.anim.slide_in_bottom,
        com.nicodelee.beautyarticle.base.R.anim.slide_out_bottom);
    appMsg.setLayoutGravity(Gravity.CENTER);
    appMsg.show();
  }

  public void skipIntent(Class clz, HashMap<String, Object> map, boolean isFinish) {
    Intent intent = new Intent(getActivity(), clz);
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
    if (isFinish) getActivity().finish();
  }

  public void skipIntent(Class clz, HashMap<String, Object> map, int code) {
    Intent intent = new Intent(getActivity(), clz);
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
    Intent intent = new Intent(getActivity(), clz);
    startActivityForResult(intent, code);
    if (isFinish) getActivity().finish();
  }

  public void skipIntent(Class clz, boolean isFinish) {
    Intent intent = new Intent(getActivity(), clz);
    startActivity(intent);
    if (isFinish) getActivity().finish();
  }

  public Object getExtra(String name) {
    return getActivity().getIntent().getSerializableExtra(name);
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

  protected void injectorPresenter() {
  }

  protected AppComponent getAppComponent() {
    return ((APP) getActivity().getApplication()).getApplicationComponent();
  }

  protected ApiComponent getApiComponent() {
    return ((APP) getActivity().getApplication()).getApiComponent();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
