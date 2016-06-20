package com.nicodelee.beautyarticle.utils;

/**
 * Description 设备相关类
 *
 * Author lirizhi
 * Create date 2013-12-29
 */

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class DevicesUtil {

  public static int screenWidth = 0;
  public static int screenHeight = 0;
  public static int statusBar = 0;

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  public static int dip2px(Context context, double dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * 将px值转换为sp值，保证文字大小不变
   *DisplayMetrics类中属性scaledDensity）
   */
  public static int px2sp(Context context, float pxValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  /**
   * 将sp值转换为px值，保证文字大小不变
   * DisplayMetrics类中属性scaledDensity）
   */
  public static int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  // 获取屏幕的高度和宽度
  public static void getScreenConfig(Context context) {

    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
    int height = wm.getDefaultDisplay().getHeight();// 屏幕高度

    Class<?> c = null;
    Object obj = null;
    Field field = null;
    // 获取屏幕状态栏的高度
    int x = 0, sbar = 0;
    try {
      c = Class.forName("com.android.internal.R$dimen");
      obj = c.newInstance();
      field = c.getField("status_bar_height");
      x = Integer.parseInt(field.get(obj).toString());
      sbar = context.getResources().getDimensionPixelSize(x);
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    screenWidth = width;
    screenHeight = height - sbar;
    statusBar = sbar;
  }
}