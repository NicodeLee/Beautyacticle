package com.nicodelee.beautyarticle.viewhelper.viewtoimage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.utils.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams;

/**
 * Created by NocodeLee on 16/6/3.
 * Email：lirizhilirizhi@163.com
 */
public class ViewToImageHelper {

  public static final String SHARE_IMAGE_CACHE = "Beauty_Cache_";
  private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
  private IViewToImage iViewToImage;
  private static int defQuality = 85;//图片压缩质量

  public ViewToImageHelper(IViewToImage iViewToImage) {
    this.iViewToImage = iViewToImage;
  }

  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  public void createImageAndShare(final Activity context, final SaveImageAction action,
      final View compressView) {
    Runtime runtime = Runtime.getRuntime();
     final int viewWidth = compressView.getWidth();
     final int viewHeight;

    if (compressView instanceof CoordinatorLayout){
      NestedScrollView scrollView =
          (NestedScrollView) ((CoordinatorLayout) compressView).getChildAt(1);
      viewHeight = scrollView.getChildAt(0).getHeight() + viewWidth;
    }else if (compressView instanceof NestedScrollView ){
      viewHeight = ((NestedScrollView) compressView).getChildAt(0).getHeight();
    }
    else{
      viewHeight = compressView.getHeight();
    }

    Logger.e("viewWidth" + viewWidth +",viewHeight"+viewHeight);

    if (((long) (((compressView.getWidth() * compressView.getHeight()) * 4) + 20971520))
        > runtime.maxMemory() - runtime.totalMemory()) {
      //布局大小超过最大限制.
      Logger.e("布局大小超过最大限制");
    } else {
      new AsyncTask<Void, Void, File>() {
        protected void onPreExecute() {
        }

        protected File doInBackground(Void... params) {
          Bitmap contentBitmap =
              Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
          final Canvas canvas = new Canvas(contentBitmap);
          canvas.save();
          File fileToShare;
          context.runOnUiThread(new Runnable() {
            @Override public void run() {
              compressView.draw(canvas);
            }
          });
          canvas.restore();
          IMAGE type = contentBitmap.getHeight() > 16000 ? IMAGE.JPEG : IMAGE.PNG;
          if (action == SaveImageAction.SAVELOCAL) {
            fileToShare = new File(ViewToImageUtils.getCacheFolderName(),
                "Beauty_" + mSimpleDateFormat.format(new Date()) + type.value);
          } else {
            fileToShare = TempFileProvider.getSharedImage(context,
                SHARE_IMAGE_CACHE + mSimpleDateFormat.format(new Date()) + type.value);
          }
          Bitmap.CompressFormat format =
              type == IMAGE.PNG ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
          if (action == SaveImageAction.SHARE) {
            while (true) {
              defQuality -= 2;
              try {
                if (!ViewToImageUtils.isLarge5M(
                    compressBitmapToImage(contentBitmap, fileToShare, format, defQuality))) {
                  break;
                }
              } catch (Throwable throwable) {
                throwable.printStackTrace();
              }
              if (format == Bitmap.CompressFormat.PNG) {
                String path = fileToShare.getAbsolutePath();
                File file = new File(
                    path.subSequence(0, path.lastIndexOf(".")).toString() + IMAGE.JPEG.value);
                fileToShare.delete();
                fileToShare = file;
                format = Bitmap.CompressFormat.JPEG;
              }
              Logger.d("FileToShare: " + fileToShare.getAbsolutePath() + "  Quality:" + defQuality);
            }
          } else {
            try {
              compressBitmapToImage(contentBitmap, fileToShare, format, defQuality);
            } catch (Throwable throwable) {
              throwable.printStackTrace();
            }
          }
          contentBitmap.recycle();
          Logger.d("FileToShare: " + fileToShare.getAbsolutePath() + "  Quality:" + defQuality);
          canvas.setBitmap(null);
          System.gc();
          return fileToShare;
        }

        protected void onPostExecute(File result) {
          super.onPostExecute(result);
          //Logger.e("保存成功:" + result.getAbsolutePath());
          iViewToImage.saveToFile(result,action);
        }
      }.execute(new Void[0]);
    }
  }

  private File compressBitmapToImage(Bitmap contentBitmap, File fileToShare,
      Bitmap.CompressFormat format, int quality) throws Throwable {
    Exception e;
    Throwable th;
    FileOutputStream out = null;
    if (fileToShare != null) {
      try {
        FileOutputStream out2 = new FileOutputStream(fileToShare);
        try {
          contentBitmap.compress(format, quality, out2);
          out2.flush();
          out2.close();
          try {
            out2.close();
          } catch (IOException e2) {
            e2.printStackTrace();
            Logger.d("FileToShare:  compressBitmapToImage out error  " + e2.getLocalizedMessage());
            out = out2;
          }
        } catch (Exception e3) {
          e = e3;
          out = out2;
          try {
            e.printStackTrace();
            Logger.d("FileToShare:  compressBitmapToImage error  " + e.getLocalizedMessage());
            try {
              out.close();
            } catch (IOException e22) {
              e22.printStackTrace();
              Logger.d(
                  "FileToShare:  compressBitmapToImage out error  " + e22.getLocalizedMessage());
            }
            return fileToShare;
          } catch (Throwable th2) {
            th = th2;
            try {
              out.close();
            } catch (IOException e222) {
              e222.printStackTrace();
              Logger.d(
                  "FileToShare:  compressBitmapToImage out error  " + e222.getLocalizedMessage());
            }
            throw th;
          }
        } catch (Throwable th3) {
          th = th3;
          out = out2;
          out.close();
          throw th;
        }
      } catch (Exception e4) {
        e = e4;
        e.printStackTrace();
        Logger.d("FileToShare:  compressBitmapToImage error  " + e.getLocalizedMessage());
        if (out != null) {
          out.close();
        }
        return fileToShare;
      }
    }
    return fileToShare;
  }

  //文章内容需要添加配图后再分享
  public View addImageViewToLayout(LayoutInflater layoutInflater,String srcHead,String srcText){
    Logger.e("srcHead="+srcHead+",srcText="+srcText);
    View viewRoot = layoutInflater.inflate(R.layout.conver_to_picture,null,false);
    ImageView ivHead = (ImageView)viewRoot.findViewById(R.id.head_pic);
    ImageView ivText = (ImageView)viewRoot.findViewById(R.id.text_pic);
    APP.getInstance().imageLoader.displayImage(srcHead,ivHead,APP.options);
    APP.getInstance().imageLoader.displayImage(srcText,ivText,APP.options);
    ViewGroup viewGroup = (ViewGroup) viewRoot;
    viewGroup.removeAllViews();
    //ivHead.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    //ivText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    //viewGroup.addView(ivText);
    viewGroup.addView(ivHead);
    viewGroup.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    //Logger.e("viewGroup width="+viewGroup.getWidth()+",getHeight="+viewGroup.getHeight());
    return viewGroup;
  }

  public enum SaveImageAction {
    SAVELOCAL,
    SHARE,
    ADD,
  }

  public enum IMAGE {
    PNG(".png"),
    JPEG(".jpeg");
    public String value;

    IMAGE(String value) {
      this.value = value;
    }
  }

  public interface IViewToImage {
    void saveToFile(File file,SaveImageAction saveImageAction);
  }
}
