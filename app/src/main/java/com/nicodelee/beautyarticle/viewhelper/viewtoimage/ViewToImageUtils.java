package com.nicodelee.beautyarticle.viewhelper.viewtoimage;

import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.Logger;
import java.io.File;

/**
 * Created by NocodeLee on 16/6/2.
 * Email：lirizhilirizhi@163.com
 */
public class ViewToImageUtils {

  public static final String NOTES_CACHE_FOLDER = "Beautyacticle/saves";

  public static File getCacheFolderName() {
    File folder = new File(Environment.getExternalStorageDirectory(), NOTES_CACHE_FOLDER);
    if (folder.exists() || folder.mkdirs()) {
      return folder;
    }
    return null;
  }

  public static boolean isImageSaved(String fileName) {
    return (fileName.startsWith("content") || fileName.startsWith("file")) ? false : true;
  }


  public static boolean isLarge5M(File file) {
    Logger.d(
        "isLarge5M: fileName:" + file.getName() + " length:byte " + file.length() + " len M:" + ((
            file.length()
                / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)
            / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
    if (file.length() > 5242880) {
      return true;
    }
    return false;
  }


}