package com.nicodelee.beautyarticle.utils;

import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by Nicodelee on 15/8/12.
 */
public class FileUtil {

  /**
   * Returns if the given file is not null and exists in the file system
   */
  public static boolean isFileValid(@Nullable File file) {
    return file != null && file.exists();
  }

  /**
   * Returns the file size of the directory
   */
  public static long getDirectorySize(File directory) {
    if (!isFileValid(directory) || !directory.isDirectory()) {
      return 0;
    }

    long size = 0;
    for (File file : directory.listFiles()) {
      size += file.length();
    }

    return size;
  }

  /**
   * Returns a human readable file size
   */
  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }
}
