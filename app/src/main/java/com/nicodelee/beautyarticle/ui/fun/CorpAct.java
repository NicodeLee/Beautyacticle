package com.nicodelee.beautyarticle.ui.fun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.utils.BitmapHelper;
import com.nicodelee.view.CropImageView;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by alee on 2015/9/4.
 */
public class CorpAct extends BaseAct {

  @Bind(R.id.cropImageView) CropImageView mCropImageView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_corp_photo);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
  }

  public void onEvent(String imagePath) {
    mCropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
    mCropImageView.setImageBitmap(new BitmapHelper().getBitmapByPath(imagePath));
  }

  @OnClick({ R.id.corp_done, R.id.corp_cancel }) public void Click(View view) {
    switch (view.getId()) {
      case R.id.corp_done:
        EventBus.getDefault().postSticky(mCropImageView.getCroppedBitmap());
        finish();
        break;
      case R.id.corp_cancel:
        finish();
        break;
    }
  }
}
