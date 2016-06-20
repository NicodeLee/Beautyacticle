package com.nicodelee.beautyarticle.ui.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Bind;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.viewhelper.viewtoimage.ViewToImageHelper;
import java.io.File;

public class SharePreAct extends BaseSwiBackAct {

  @Bind(R.id.head_pic) ImageView headPic;
  @Bind(R.id.text_pic) SubsamplingScaleImageView textPic;
  @Bind(R.id.toolbar) protected Toolbar toolbar;
  @Bind(R.id.sc_acticle) NestedScrollView scActicle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initView();
  }

  private void initView() {

    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDisplayHomeAsUpEnabled(true);
      ab.setTitle("分享预览");
    }

    String headSrc = (String) getExtra("haedpic");
    String textSrc = (String) getExtra("textPic");
    APP.getInstance().imageLoader.displayImage(headSrc, headPic, APP.options);
    textPic.setImage(ImageSource.uri(textSrc));
    textPic.setZoomEnabled(false);
    int width = DevicesUtil.screenWidth - DevicesUtil.dip2px(this, 32);
    headPic.setLayoutParams(
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width));
  }

  @Override protected int getLayoutResId() {
    return R.layout.conver_to_picture;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu_share, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
      case R.id.menu_share:
        ShareAction(ViewToImageHelper.SaveImageAction.SHARE);
        break;
      case R.id.menu_save:
        ShareAction(ViewToImageHelper.SaveImageAction.SAVELOCAL);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void ShareAction(ViewToImageHelper.SaveImageAction saveImageAction) {
    new ViewToImageHelper(new ViewToImageHelper.IViewToImage() {
      @Override
      public void saveToFile(File file, ViewToImageHelper.SaveImageAction saveImageAction) {
        switch (saveImageAction) {
          case SHARE:
            SharImageHelper sharImageHelper = new SharImageHelper();
            Bitmap bitmap =
                APP.getInstance().imageLoader.loadImageSync("file://" + file.getAbsolutePath());
            sharImageHelper.saveBitmap(bitmap, SharImageHelper.sharePicName);
            ShareHelper.showUp(SharePreAct.this, sharImageHelper.getShareMod(bitmap));
            break;
          case SAVELOCAL:
            showInfo("图片保存路径:" + file.getAbsolutePath());
            break;
        }
      }
    }).createImageAndShare(SharePreAct.this, saveImageAction, scActicle);
  }
}
