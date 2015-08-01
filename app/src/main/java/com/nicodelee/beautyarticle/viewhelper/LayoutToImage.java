package com.nicodelee.beautyarticle.viewhelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class LayoutToImage {
    private View view;
    private Context context;
    private Bitmap bMap;

    public LayoutToImage(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public Bitmap convertlayout() {

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);

        bMap = Bitmap.createBitmap(view.getDrawingCache());

        return bMap;
    }
}
