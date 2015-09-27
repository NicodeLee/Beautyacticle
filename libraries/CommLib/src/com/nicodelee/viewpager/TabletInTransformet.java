package com.nicodelee.viewpager;

import android.view.View;

/**
 * Created by alee on 2015/9/28.
 */
public class TabletInTransformet extends ABaseTransformer {

  @Override
  protected void onTransform(View view, float position) {
    final float rotation = (position < 0 ? 30f : -30f) * Math.abs(position);

  }
}
