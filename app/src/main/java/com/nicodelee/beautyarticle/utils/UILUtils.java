package com.nicodelee.beautyarticle.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nicodelee on 15/7/7.
 */
public class UILUtils {
  public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
    static final List<String> displayedImages =
        Collections.synchronizedList(new LinkedList<String>());

    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 500);//动画效果
          displayedImages.add(imageUri);
        }
      }
    }
  }
}
