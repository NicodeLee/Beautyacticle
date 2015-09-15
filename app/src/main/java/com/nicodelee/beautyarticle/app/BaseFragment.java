package com.nicodelee.beautyarticle.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseFragment extends Fragment {
  private Context context;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getActivity().getApplicationContext();
    //RefWatcher refWatcher = APP.getRefWatcher(getActivity());
    //refWatcher.watch(this);
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
    if (isStickyAvailable()) {
      EventBus.getDefault().register(this);
    } else {
      EventBus.getDefault().registerSticky(this);
    }
    super.onStart();
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  public void onEvent(Object event) {
  }

  protected boolean isStickyAvailable() {
    return false;
  }
}
