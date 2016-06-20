package com.nicodelee.beautyarticle.viewhelper.viewtoimage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.IOException;

/**
 * Created by NocodeLee on 16/6/2.
 * Email：lirizhilirizhi@163.com
 */
public class TempFileProvider extends ContentProvider {
  public static String AUTHORITY = null;
  private static String TAG = "TempFileProvider";
  private static final UriMatcher sURLMatcher = new UriMatcher(-1);

  static {
    AUTHORITY = "";
    sURLMatcher.addURI(AUTHORITY, "scrapSpace", 1);
    sURLMatcher.addURI(AUTHORITY, "scrapSpace/*", 2);
  }

  public boolean onCreate() {
    return true;
  }

  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    return null;
  }

  public Uri insert(Uri uri, ContentValues values) {
    return null;
  }

  public int delete(Uri uri, String selection, String[] selectionArgs) {
    return 0;
  }

  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return 0;
  }

  public String getType(Uri uri) {
    return "*/*";
  }

  public static String getScrapPath(Context context, String fileName) {
    String filePath = null;
    File file = context.getExternalCacheDir();
    if (file == null) {
      file = context.getCacheDir();
    }
    if (file != null) {
      filePath = file.getAbsolutePath();
    }
    File noMedia = new File(filePath, ".nomedia");
    if (!noMedia.exists()) {
      try {
        noMedia.createNewFile();
      } catch (IOException e) {
        Log.e(TAG, "Can't create the nomedia, e:" + e);
      }
    }
    return filePath + "/" + fileName;
  }

  public static final String SHARE_IMAGE_CACHE = "Share_Cache_";

  public static File getSharedImage(Context context, String name) {
    File cacheDir = new File(getScrapPath(context, ""));
    if (cacheDir.exists()) {
      File[] files = cacheDir.listFiles();
      long now = System.currentTimeMillis();
      for (File file : files) {
        if (file.getName().startsWith(SHARE_IMAGE_CACHE) && now - file.lastModified() > 1800000) {
          file.delete();
        }
      }
    } else {
      cacheDir.mkdir();
    }
    File result = new File(getScrapPath(context, name));
    if (!result.exists()) {
      try {
        result.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static String getScrapPath(Context context) {
    return getScrapPath(context, ".temp");
  }

  public static Uri renameScrapFile(String fileExtension, String uniqueIdentifier,
      Context context) {
    String filePath = getScrapPath(context);
    if (uniqueIdentifier == null) {
      uniqueIdentifier = "";
    }
    File newTempFile = new File(getScrapPath(context, ".temp" + uniqueIdentifier + fileExtension));
    File oldTempFile = new File(filePath);
    boolean deleted = newTempFile.delete();
    if (oldTempFile.renameTo(newTempFile)) {
      return Uri.fromFile(newTempFile);
    }
    return null;
  }

  public static boolean isTempFile(Context context, String path) {
    String tempPath = getScrapPath(context);
    return path.startsWith(tempPath) || path.startsWith("file://" + tempPath);
  }
}