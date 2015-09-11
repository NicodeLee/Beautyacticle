package com.nicodelee.beautyarticle.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicodelee on 15/9/6.
 */
public class BitmapHelper {

  /**
   * see:
   * http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/load-bitmap.html
   * http://stackoverflow.com/questions/26465281/outofmemoryerror-in-bitmapfactory-decodefile
   */
  public Bitmap getBitmapByPath(String filePath) {
    try {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, options);
      final int REQUIRED_WIDTH = 750;
      final int REQUIRED_HIGHT = 1000;
      int scale = 1;
      while (options.outWidth / scale / 2 >= REQUIRED_WIDTH && options.outHeight / scale / 2 >= REQUIRED_HIGHT)
        scale *= 2;

      BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = scale;
      return BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, o2);
    } catch (FileNotFoundException e) {
    }
    return null;
  }

}
