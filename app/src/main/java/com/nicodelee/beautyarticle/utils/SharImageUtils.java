package com.nicodelee.beautyarticle.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nicodelee on 15/8/1.
 */
public class SharImageUtils {


    public static void share(String imgPath, String content, Context context) {
        File f = new File(imgPath);
        Uri uri = Uri.fromFile(f);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null && imgPath != null && imgPath != "") {
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpeg");
            // 当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", content);
        } else {
            shareIntent.setType("text/plain");
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        // 自定义选择框的标题
        context.startActivity(Intent.createChooser(shareIntent, "选择分享方式"));
        // 系统默认标题
//        context.startActivity(shareIntent);
    }

    /** 保存方法 */
    public static boolean saveBitmap(Bitmap bitmap,String picName) {
        File f = new File(AndroidUtils.IMAGE_CACHE_PATH, picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
