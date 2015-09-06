package com.nicodelee.beautyarticle.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicodelee on 15/9/6.
 */
public class BitmapHelper {

  private Map<String, SoftReference<Bitmap>> imageCache;
  public BitmapHelper(){
    imageCache = new HashMap<String, SoftReference<Bitmap>>();
  }

  public void addBitmapToCache(String path) {
    BitmapFactory.Options bfOpt = new BitmapFactory.Options();
    bfOpt.inSampleSize = 2; // 缩小为1/2显示减少内存消耗
    Bitmap bitmap = BitmapFactory.decodeFile(path, bfOpt);
    SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
    imageCache.put(path, softBitmap);
  }

  public Bitmap getBitmapByPath(String path) {
    addBitmapToCache(path);
    SoftReference<Bitmap> softBitmap = imageCache.get(path);
    if (softBitmap == null) {
      return null;
    }
    Bitmap bitmap = softBitmap.get();
    return bitmap;
  }
}
