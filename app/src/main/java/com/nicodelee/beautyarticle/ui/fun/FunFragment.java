package com.nicodelee.beautyarticle.ui.fun;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.clans.fab.FloatingActionMenu;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.bus.CropEvent;
import com.nicodelee.beautyarticle.mode.ShareMod;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.beautyarticle.viewhelper.VerticalTextView;
import com.nicodelee.utils.WeakHandler;
import com.nicodelee.view.CircularImage;
import com.nicodelee.view.CropImageView;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static butterknife.ButterKnife.findById;

public class FunFragment extends BaseFragment {

  @Bind(R.id.sc_fun) NestedScrollView scFun;
  @Bind(R.id.rl_fun) RelativeLayout rlFun;
  @Bind(R.id.tv_desc) VerticalTextView tvDesc;
  @Bind(R.id.tv_time) TextView tvTime;
  @Bind(R.id.tv_title) TextView tvTitle;
  @Bind(R.id.fl_menu) FloatingActionMenu famFun;
  @Bind(R.id.iv_fun) CircularImage ivFun;

  @BindString(R.string.article) String acticle;
  @BindString(R.string.acticle_title) String acticleTitle;

  private LayoutToImage layoutToImage;
  //private Bitmap bitmap;
  private LayoutInflater inflater;

  private String title, desc;
  private static final int REQUEST_IMAGE = 2;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_fun, container, false);
    ButterKnife.bind(this, view);
    init();
    return view;
  }

  private void init() {
    inflater = LayoutInflater.from(mActivity);
    famFun.setClosedOnTouchOutside(true);

    tvTitle.setText(acticleTitle);
    tvDesc.setTextSize(DevicesUtil.sp2px(mActivity, 18));
    tvDesc.setLineWidth(DevicesUtil.dip2px(mActivity, 30));
    Typeface face = Typeface.createFromAsset(mActivity.getAssets(), "fonts/fun_font.TTF");
    tvDesc.setTextColor(R.color.templage_text);
    tvDesc.setTypeface(face);
    tvDesc.setText(acticle);
    tvTime.setText(TimeUtils.dateToCnDate(TimeUtils.getCurentData()));

    rlFun.setLayoutParams(new LayoutParams(DevicesUtil.screenWidth, LayoutParams.MATCH_PARENT));
    layoutToImage = new LayoutToImage(scFun);
  }

  @OnClick({ R.id.fb_share, R.id.fb_make, R.id.iv_fun, R.id.fb_more })
  public void Click(View view) {
    famFun.close(true);
    switch (view.getId()) {
      case R.id.fb_share:

        final SharImageHelper sharImageHelper = new SharImageHelper();
        //if (sharImageHelper.saveBitmap(bitmap, SharImageHelper.sharePicName)) {
        //  ShareHelper.showUp(mActivity, sharImageHelper.getShareMod(bitmap));
        //}
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
          @Override public void call(Subscriber<? super Bitmap> subscriber) {
            Bitmap bitmap = layoutToImage.convertlayout();
            sharImageHelper.saveBitmap(bitmap, SharImageHelper.sharePicName);
            subscriber.onNext(bitmap);
            subscriber.onCompleted();
          }
        })
            //.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Bitmap>() {
                  @Override public void call(Bitmap bitmap) {
                    if (bitmap != null) {
                      ShareHelper.showUp(mActivity, sharImageHelper.getShareMod(bitmap));
                    }
                  }
                });

        break;
      case R.id.fb_make:
        showEdDialig();
        break;
      case R.id.fb_more:
        skipIntent(FunTemplateAct.class, false);
        break;
      case R.id.iv_fun:
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        MultiImageSelectorActivity.startSelect(FunFragment.this, REQUEST_IMAGE, 1, selectedMode);
        break;
    }
  }

  private void showEdDialig() {
    View etView = inflater.inflate(R.layout.item_fun_et, null);
    final EditText etTitle = findById(etView, R.id.et_title);
    final EditText etDesc = findById(etView, R.id.et_desc);
    etTitle.setText(title);
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

            title = etTitle.getText().toString().trim();
            desc = etDesc.getText().toString().trim();
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)) {
              tvTitle.setText(title);
              tvDesc.setText(desc);
            }
            dialog.dismiss();
          }
        })
        .create()
        .show();

    new WeakHandler().postDelayed(new Runnable() {
      @Override public void run() {
        InputMethodManager inputManager = (InputMethodManager) etTitle.getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etTitle, 0);
      }
    }, 200);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == mActivity.RESULT_OK && requestCode == REQUEST_IMAGE) {
      ArrayList<String> mSelectPath =
          data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
      CropEvent cropEvent = new CropEvent();
      cropEvent.setCropMode(CropImageView.CropMode.CIRCLE);
      cropEvent.setImagePath(mSelectPath.get(0));
      EventBus.getDefault().postSticky(cropEvent);
      skipIntent(CropAct.class, false);
    }
  }

  public void onEvent(Bitmap corpBitmap) {
    ivFun.setImageBitmap(corpBitmap);
  }
}
