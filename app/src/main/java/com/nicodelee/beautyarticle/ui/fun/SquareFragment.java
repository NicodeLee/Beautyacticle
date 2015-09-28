package com.nicodelee.beautyarticle.ui.fun;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.clans.fab.FloatingActionMenu;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.bus.CropEvent;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.utils.WeakHandler;
import com.nicodelee.view.CropImageView;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static butterknife.ButterKnife.findById;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class SquareFragment extends TemplateBase {

  public static final String EXTRA_POSITION = "ARTICLE_POSITION";
  @Bind(R.id.iv_fun) ImageView ivFun;
  @Bind(R.id.fl_menu) FloatingActionMenu famFun;
  @Bind(R.id.sc_fun) ScrollView scFun;
  @Bind(R.id.tv_fun) TextView tvDesc;

  private static final int REQUEST_IMAGE = 2;

  private LayoutToImage layoutToImage;
  private Bitmap bitmap;
  private LayoutInflater inflater;
  private String title, desc;

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
    params.width = DevicesUtil.screenWidth;
    params.height = DevicesUtil.screenWidth;
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
        showEdDialig();
        break;
      case R.id.fb_more:
      case R.id.iv_fun:
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        MultiImageSelectorActivity.startSelect(SquareFragment.this, REQUEST_IMAGE, 1, selectedMode);
        break;
    }
  }

  private void showEdDialig() {
    View etView = inflater.inflate(R.layout.item_fun_et, null);
    final EditText etDesc = findById(etView, R.id.et_desc);
    final EditText etTitle = findById(etView, R.id.et_title);
    etTitle.setVisibility(View.GONE);
    etDesc.setText(desc);

    new AlertDialog.Builder(mActivity).setMessage("发挥你聪明才智")
        .setView(etView)
        .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .setPositiveButton("发表", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {

            desc = etDesc.getText().toString().trim();
            if (!TextUtils.isEmpty(desc)) {
              tvDesc.setText(desc);
            }
            dialog.dismiss();
          }
        })
        .create()
        .show();

    new WeakHandler().postDelayed(new Runnable() {
      @Override public void run() {
        InputMethodManager inputManager =
            (InputMethodManager) etDesc.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etDesc, 0);
      }
    }, 200);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == mActivity.RESULT_OK && requestCode == REQUEST_IMAGE) {
      ArrayList<String> mSelectPath =
          data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
      CropEvent cropEvent = new CropEvent();
      cropEvent.setCropMode(CropImageView.CropMode.RATIO_1_1);
      cropEvent.setImagePath(mSelectPath.get(0));
      EventBus.getDefault().postSticky(cropEvent);
      skipIntent(CropAct.class, false);
    }
  }

  public void onEvent(Bitmap corpBitmap) {
    ivFun.setImageBitmap(corpBitmap);
  }
}
