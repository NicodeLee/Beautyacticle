package com.nicodelee.beautyarticle.ui.fun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.bus.CropEvent;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.view.CropImageView;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class RectangleFragment extends TemplateBase {


  public static final String EXTRA_POSITION = "ARTICLE_POSITION";
  private int position;

  private Bitmap bitmap;
  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_square, container, false);
    position = getArguments().getInt(EXTRA_POSITION);
    ButterKnife.bind(this, view);
    initView();
    return view;
  }

  private void initView() {
    inflater = LayoutInflater.from(mActivity);
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFun.getLayoutParams();
    int width = DevicesUtil.screenWidth - DevicesUtil.dip2px(getActivity(),16f);
    params.width = width;
    if (position == 1){
      params.height = (int)(width * 1.3);
    }else if (position == 2){
      params.height = (int)(width * 0.75);
    }
    ivFun.setLayoutParams(params);
    layoutToImage = new LayoutToImage(scFun);
  }

  @OnClick({ R.id.fb_share, R.id.fb_make, R.id.iv_fun }) public void Click(View view) {
    famFun.close(true);
    switch (view.getId()) {
      case R.id.fb_share:
        bitmap = layoutToImage.convertlayout();
        SharImageHelper sharImageHelper = new SharImageHelper();
        if (sharImageHelper.saveBitmap(bitmap, SharImageHelper.sharePicName)) {
          ShareHelper.showUp(mActivity, sharImageHelper.getShareMod(bitmap));
        }

        break;
      case R.id.fb_make:
        showEdDialig(false);
        break;
      case R.id.iv_fun:
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        MultiImageSelectorActivity.startSelect(RectangleFragment.this, REQUEST_IMAGE, 1, selectedMode);
        break;
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == mActivity.RESULT_OK && requestCode == REQUEST_IMAGE) {
      ArrayList<String> mSelectPath =
          data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
      CropEvent cropEvent = new CropEvent();
      if (position == 1){
        cropEvent.setCropMode(CropImageView.CropMode.RATIO_3_4);
      }else if (position == 2){
        cropEvent.setCropMode(CropImageView.CropMode.RATIO_4_3);
      }

      cropEvent.setImagePath(mSelectPath.get(0));
      EventBus.getDefault().postSticky(cropEvent);
      skipIntent(CropAct.class, false);
    }
  }

  public void onEvent(Bitmap corpBitmap) {
    ivFun.setImageBitmap(corpBitmap);
  }
}
