package com.nicodelee.beautyarticle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.mode.ShareMod;
import com.nicodelee.beautyarticle.viewhelper.ShareButton;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * see:http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/
 */

public class ShareHelper {

  private static void shareAction(Context context, AlertDialog dialog) {
    dialog.dismiss();
    Toast.makeText(context, context.getString(R.string.share_tip), Toast.LENGTH_SHORT).show();
  }

  public static void showUp(final Context context, final ShareMod shareMod) {
    ShareSDK.initSDK(context);

    View v = LayoutInflater.from(context).inflate(R.layout.layout_share, null, false);
    final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
    dialog.show();
    ShareButton weibo = (ShareButton) v.findViewById(R.id.action_share_weibo);
    ShareButton wechat = (ShareButton) v.findViewById(R.id.action_share_wechat);
    ShareButton wechatTimeline = (ShareButton) v.findViewById(R.id.action_share_wechat_timeline);
    ShareButton qq = (ShareButton) v.findViewById(R.id.action_share_qq);
    ShareButton link = (ShareButton) v.findViewById(R.id.action_share_this);
    ShareButton qzone = (ShareButton) v.findViewById(R.id.action_share_qzone);

    final TextView cancel = (TextView) v.findViewById(R.id.cancel);

    final Handler showMsg = new Handler(new Handler.Callback() {
      @Override public boolean handleMessage(Message msg) {
        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        return true;
      }
    });

    final PlatformActionListener platformActionListener = new PlatformActionListener() {
      @Override public void onComplete(Platform platform, int i,
          HashMap<String, Object> stringObjectHashMap) {
        Message msg = Message.obtain(showMsg);
        msg.obj = context.getString(R.string.share_success);
        msg.sendToTarget();
      }

      @Override public void onError(Platform platform, int i, Throwable throwable) {
        L.e("@@@" + throwable.getMessage() + "," + platform.getName());
        Message msg = Message.obtain(showMsg);
        msg.obj = context.getString(R.string.share_error);
        msg.sendToTarget();
      }

      @Override public void onCancel(Platform platform, int i) {
        Message msg = Message.obtain(showMsg);
        msg.obj = context.getString(R.string.share_cancel);
        msg.sendToTarget();
      }
    };

    weibo.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        Platform pf = ShareSDK.getPlatform(context, SinaWeibo.NAME);
        sp.setText(shareMod.text);
        sp.setUrl(shareMod.url);
        sp.setImageData(shareMod.imageData);
        sp.setImagePath(shareMod.imageUrl);
        sp.setShareType(Platform.SHARE_IMAGE);
        pf.setPlatformActionListener(platformActionListener);
        pf.share(sp);
        shareAction(context, dialog);
      }
    });

    wechat.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Platform plat = ShareSDK.getPlatform(context, Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle(shareMod.title);
        sp.setText(shareMod.text);
        sp.setImageUrl(shareMod.imageUrl);
        Bitmap imageData = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        sp.setImageData(shareMod.imageData);
        sp.setUrl(shareMod.url);
        sp.setShareType(Platform.SHARE_IMAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
        shareAction(context, dialog);
      }
    });

    wechatTimeline.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Platform plat = ShareSDK.getPlatform(context, WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.title = shareMod.title;
        sp.text = shareMod.text;
        sp.imageUrl = shareMod.imageUrl;
        sp.url = shareMod.url;
        sp.imageData = shareMod.imageData;
        sp.shareType = Platform.SHARE_IMAGE;
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
        shareAction(context, dialog);
      }
    });

    qq.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Platform plat = ShareSDK.getPlatform(context, QQ.NAME);
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(shareMod.title);
        sp.setText(shareMod.text);
        sp.setTitleUrl(shareMod.titleUrl);
        sp.setImagePath(shareMod.imageUrl);
        sp.setShareType(QQ.SHARE_IMAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
        shareAction(context, dialog);
      }
    });

    qzone.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Platform plat = ShareSDK.getPlatform(context, QZone.NAME);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(shareMod.title);
        sp.setText(shareMod.text);
        sp.setImageUrl(shareMod.imageUrl);
        sp.setTitleUrl(shareMod.titleUrl);
        sp.setImageData(shareMod.imageData);
        sp.setImagePath(shareMod.imageUrl);
        sp.setShareType(QZone.SHARE_IMAGE);
        plat.setPlatformActionListener(platformActionListener);
        plat.share(sp);
        shareAction(context, dialog);
      }
    });

    link.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new SharImageHelper().share(shareMod.imageUrl, "分享", context);
        dialog.dismiss();
      }
    });

    cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }
}
