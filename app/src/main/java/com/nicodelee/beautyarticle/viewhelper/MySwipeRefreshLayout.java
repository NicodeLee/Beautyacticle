package com.nicodelee.beautyarticle.viewhelper;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by Nicodelee on 15/3/31.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
  public MySwipeRefreshLayout(Context context) {
    super(context);
  }

  public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  private boolean mMeasured = false;
  private boolean mPreMeasureRefreshing = false;

  @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (!mMeasured) {
      mMeasured = true;
      setRefreshing(mPreMeasureRefreshing);
    }
  }

  @Override public void setRefreshing(boolean refreshing) {
    if (mMeasured) {
      super.setRefreshing(refreshing);
    } else {
      mPreMeasureRefreshing = refreshing;
    }
  }
}
