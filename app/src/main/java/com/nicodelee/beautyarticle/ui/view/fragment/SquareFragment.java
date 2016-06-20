package com.nicodelee.beautyarticle.ui.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.bus.CropEvent;
import com.nicodelee.beautyarticle.ui.view.activity.CropAct;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.Logger;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.view.CropImageView;
import java.util.ArrayList;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class SquareFragment extends TemplateBase {

  @Bind(R.id.tv_month) TextView tvMonth;
  @Bind(R.id.tv_year) TextView tvYear;
  private static final int REQUEST_IMAGE = 2;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_square, container, false);
    ButterKnife.bind(this, view);
    initView();
    return view;
  }

  private void initView() {
    inflater = LayoutInflater.from(mActivity);
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFun.getLayoutParams();
    int width = DevicesUtil.screenWidth;
    params.width = width;
    params.height = width;
    ivFun.setLayoutParams(params);
    layoutToImage = new LayoutToImage(scFun);

    tvMonth.setText(TimeUtils.getEnMonth() + " " + TimeUtils.getSimpleDay());
    tvYear.setText(TimeUtils.getSimpleYear());
  }

  @OnClick({ R.id.fb_share, R.id.fb_make, R.id.iv_fun }) public void Click(View view) {
    famFun.close(true);
    switch (view.getId()) {
      case R.id.fb_share:
        Bitmap bitmap = layoutToImage.convertlayout();
        SharImageHelper sharImageHelper = new SharImageHelper();
        String picName = SharImageHelper.sharePicName;
        if (sharImageHelper.saveBitmap(bitmap, picName)) {
          ShareHelper.showUp(mActivity, sharImageHelper.getShareMod(bitmap));
        }

        break;
      case R.id.fb_make:
        showEdDialig(false);
        break;
      case R.id.iv_fun:
        showChiocePicDialog();
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE) {
      ArrayList<String> mSelectPath =
          data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
      CropEvent cropEvent = new CropEvent();
      cropEvent.setCropMode(CropImageView.CropMode.RATIO_1_1);
      cropEvent.setImagePath(mSelectPath.get(0));
      EventBus.getDefault().postSticky(cropEvent);
      skipIntent(CropAct.class, false);
    }
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN) public void onEvent(Bitmap corpBitmap) {
    ivFun.setImageBitmap(corpBitmap);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN) public void onEvent(Uri uri) {//拍照后编辑
    APP.getInstance().imageLoader.displayImage(uri + "", ivFun, APP.options);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
