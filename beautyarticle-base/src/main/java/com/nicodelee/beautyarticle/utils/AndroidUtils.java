package com.nicodelee.beautyarticle.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AndroidUtils {
  private static final String IMAGE_CACHE = "image";
  private static final String VIDEO_CACHE = "video";
  private static final String BASE = "Beautyacticle/";
  private static final String IMAGE_SAVE = "pic";

  public static String IMAGE_CACHE_PATH;
  public static String VIDEO_CACHE_PATH;
  public static String PIC_CACHE_PATH;
  public static String BASE_PATH;

  public static void init(Context context) {
    if (IMAGE_CACHE_PATH != null) return;
    if (sdCardIsAvailable()) {
      BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    } else {
      BASE_PATH = getExternalCacheDir(context);
    }
    IMAGE_CACHE_PATH = BASE_PATH + BASE + IMAGE_CACHE;
    VIDEO_CACHE_PATH = BASE_PATH + BASE + VIDEO_CACHE;
    PIC_CACHE_PATH = BASE_PATH + BASE + IMAGE_SAVE;
    createDirectory(IMAGE_CACHE_PATH);
    createDirectory(VIDEO_CACHE_PATH);
    createDirectory(PIC_CACHE_PATH);
  }

  public static void createDirectory(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  public static String convertOutputFormatToFileExt() {
    CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

    if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
      return ".mp4";
    }
    return ".3gp";
  }

  /**
   *
   * @param context
   * @return
   */
  public static String getDeviceId(Context context) {
    final TelephonyManager tm =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    final String tmDevice, tmSerial, tmPhone, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
        android.provider.Settings.Secure.ANDROID_ID);
    UUID deviceUuid =
        new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    String uniqueId = deviceUuid.toString();
    return uniqueId;
  }

  public static boolean sdCardIsAvailable() {
    String status = Environment.getExternalStorageState();
    if (!status.equals(Environment.MEDIA_MOUNTED)) return false;
    return true;
  }

  /**
   * Checks if there is enough Space on SDCard
   *
   * @param updateSize Size to Check
   * @return True if the Update will fit on SDCard, false if not enough space
   * on SDCard Will also return false, if the SDCard is not mounted as
   * read/write
   */
  public static boolean enoughSpaceOnSdCard(long updateSize) {
    String status = Environment.getExternalStorageState();
    if (!status.equals(Environment.MEDIA_MOUNTED)) return false;
    return (updateSize < getRealSizeOnSdcard());
  }

  /**
   * get the space is left over on sdcard
   */
  public static long getRealSizeOnSdcard() {
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    return availableBlocks * blockSize;
  }

  /**
   * Checks if there is enough Space on phone self
   */
  public static boolean enoughSpaceOnPhone(long updateSize) {
    return getRealSizeOnPhone() > updateSize;
  }

  /**
   * get the space is left over on phone self
   */
  public static long getRealSizeOnPhone() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    long realSize = blockSize * availableBlocks;
    return realSize;
  }

  /**
   *
   * @param context
   * @param dpValue
   * @return
   */
  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public static int dip2px(Context context, int rsd) {
    float dpValue = context.getResources().getDimension(rsd);
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f) - 15;
  }

  /**
   * @param context The context to use
   * @return The external cache dir
   */
  private static String getExternalCacheDir(Context context) {
    //TODO  android 2.2 ,部分手机没有内存卡会报错
    if (hasExternalCacheDir()) {
      //			return context.getExternalCacheDir().getPath() + File.separator;
    }

    // Before Froyo we need to construct the external cache dir ourselves
    final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/image/";
    return Environment.getExternalStorageDirectory().getPath() + cacheDir;
  }

  private static boolean hasExternalCacheDir() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
  }

  private static void saveFileByRequestPath(String requestPath, String result) {
    // TODO Auto-generated method stub
    deleteFileFromLocal(requestPath);
    saveFileForLocal(requestPath, result);
  }

  public static void saveFileForLocal(String requestPath, String result) {
    // TODO Auto-generated method stub
    File file = new File(requestPath);
    if (!file.exists()) {
      try {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
          parentFile.mkdirs();
        }
        file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        byte[] buffer = result.getBytes();
        fout.write(buffer);
        fout.close();
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }


  /**
   *
   * @return
   */
  public static int getStatusBarHeight(Context context) {
    Class<?> c = null;
    Object obj = null;
    java.lang.reflect.Field field = null;
    int x = 0;
    int statusBarHeight = 0;
    try {
      c = Class.forName("com.android.internal.R$dimen");
      obj = c.newInstance();
      field = c.getField("status_bar_height");
      x = Integer.parseInt(field.get(obj).toString());
      statusBarHeight = context.getResources().getDimensionPixelSize(x);
      return statusBarHeight;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return statusBarHeight;
  }

  public static int[] getScreen(Activity context) {
    DisplayMetrics dm = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
    int screenWidth = dm.widthPixels;
    int screenHeigh = dm.heightPixels;
    int[] a = new int[2];
    a[0] = screenWidth;
    a[1] = screenHeigh;
    return a;
  }

  private static void deleteFileFromLocal(String requestPath) {
    File file = new File(requestPath);
    if (file.exists()) {
      file.delete();
    }
  }

}
