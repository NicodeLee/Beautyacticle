package com.nicodelee.beautyarticle.viewhelper;

import android.graphics.Bitmap;
import android.view.View;
import com.nicodelee.beautyarticle.utils.Logger;

public class LayoutToImage {
  private View view;

  public LayoutToImage(View view) {
    this.view = view;
  }

  public Bitmap convertlayout() {

    view.setDrawingCacheEnabled(true);
    view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + 60);
    view.buildDrawingCache(true);
    Logger.e("with:"+view.getMeasuredWidth()+",heitht:"+view.getMeasuredHeight());
    return Bitmap.createBitmap(view.getDrawingCache());
  }
}
