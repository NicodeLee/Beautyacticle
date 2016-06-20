package me.imid.swipebacklayout.lib.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
  private SwipeBackActivityHelper mHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mHelper = new SwipeBackActivityHelper(this);
    mHelper.onActivityCreate();
    mHelper.getSwipeBackLayout().setSensitivity(this, 0.5f);
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mHelper.onPostCreate();
  }

  @Override public View findViewById(int id) {
    View v = super.findViewById(id);
    if (v == null && mHelper != null) return mHelper.findViewById(id);
    return v;
  }

  @Override public SwipeBackLayout getSwipeBackLayout() {
    return mHelper.getSwipeBackLayout();
  }

  @Override public void setSwipeBackEnable(boolean enable) {
    getSwipeBackLayout().setEnableGesture(enable);
  }

  @Override public void scrollToFinishActivity() {
    Utils.convertActivityToTranslucent(this);
    getSwipeBackLayout().scrollToFinishActivity();
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
}
