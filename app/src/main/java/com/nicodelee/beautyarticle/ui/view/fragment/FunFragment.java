package com.nicodelee.beautyarticle.ui.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.clans.fab.FloatingActionMenu;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.bus.CropEvent;
import com.nicodelee.beautyarticle.ui.view.activity.CameraActivity;
import com.nicodelee.beautyarticle.ui.view.activity.CropAct;
import com.nicodelee.beautyarticle.ui.view.activity.FunTemplateAct;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.Logger;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.beautyarticle.viewhelper.VerticalTextView;
import com.nicodelee.utils.StringUtils;
import com.nicodelee.utils.WeakHandler;
import com.nicodelee.view.CircularImage;
import com.nicodelee.view.CropImageView;
import java.io.File;
import java.util.ArrayList;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static butterknife.ButterKnife.findById;

public class FunFragment extends BaseFragment {

  @Bind(R.id.sc_fun) NestedScrollView scFun;
  @Bind(R.id.rl_fun) RelativeLayout rlFun;
  @Bind(R.id.tv_desc) VerticalTextView tvDesc;
  @Bind(R.id.tv_title) TextView tvTitle;
  @Bind(R.id.fl_menu) FloatingActionMenu famFun;
  @Bind(R.id.iv_fun) CircularImage ivFun;

  @BindString(R.string.article) String acticle;
  @BindString(R.string.acticle_title) String acticleTitle;
  @Bind(R.id.tv_day) TextView tvDay;
  @Bind(R.id.tv_month) TextView tvMonth;
  @Bind(R.id.tv_year) TextView tvYear;

  private LayoutToImage layoutToImage;
  private LayoutInflater inflater;

  private String title, desc;
  private static final int REQUEST_IMAGE = 2;
  private static final int REQUEST_PORTRAIT_FFC = 3;

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
    tvDesc.setTextSize(DevicesUtil.sp2px(mActivity, 16));
    tvDesc.setLineWidth(DevicesUtil.dip2px(mActivity, 24));
    Typeface face = Typeface.createFromAsset(mActivity.getAssets(), "fonts/fun_font.TTF");
    tvDesc.setTextColor(R.color.templage_title);
    tvDesc.setTypeface(face);
    tvDesc.setText(acticle);
    String date = TimeUtils.dateToCnDate(TimeUtils.getCurentData());

    tvDay.setText(TimeUtils.getDay(date));
    tvMonth.setText(TimeUtils.getMonth(date));
    tvYear.setText(TimeUtils.getYear(date));

    rlFun.setLayoutParams(
        new FrameLayout.LayoutParams(DevicesUtil.screenWidth, ViewGroup.LayoutParams.MATCH_PARENT));
    layoutToImage = new LayoutToImage(scFun);
  }

  @OnClick({ R.id.fb_share, R.id.fb_make, R.id.iv_fun, R.id.fb_more })
  public void Click(View view) {
    famFun.close(true);
    switch (view.getId()) {
      case R.id.fb_share:

        final SharImageHelper sharImageHelper = new SharImageHelper();
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
          @Override public void call(Subscriber<? super Bitmap> subscriber) {
            Bitmap bitmap = layoutToImage.convertlayout();
            sharImageHelper.saveBitmap(bitmap, SharImageHelper.sharePicName);
            subscriber.onNext(bitmap);
            subscriber.onCompleted();
          }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
          @Override public void call(Bitmap bitmap) {
            ShareHelper.showUp(mActivity, sharImageHelper.getShareMod(bitmap));
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
        showChiocePicDialog();
        break;
    }
  }

  private void showEdDialig() {
    View etView = inflater.inflate(R.layout.item_fun_et, null);
    final TextInputLayout inputTitle = findById(etView, R.id.textInput_title);
    final TextInputLayout inputDesc = findById(etView, R.id.textInput_desc);
    inputTitle.setHint("想个好标题");
    inputDesc.setHint("说说图片的故事");
    final EditText etTitle = inputTitle.getEditText();
    final EditText etDesc = inputDesc.getEditText();
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

  private void showChiocePicDialog() {
    String[] items = new String[] { "拍照", "相册" };
    new AlertDialog.Builder(mActivity).setItems(items, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case 0:
            Intent i = new CameraActivity.IntentBuilder(getActivity()).skipConfirm()
                .facing(CameraActivity.Facing.BACK)
                .to(new File(AndroidUtils.IMAGE_CACHE_PATH, "nicodelee.jpg"))
                .debug()
                .updateMediaStore()
                .build();

            startActivityForResult(i, REQUEST_PORTRAIT_FFC);
            break;
          case 1:
            int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
            MultiImageSelectorActivity.startSelect(FunFragment.this, REQUEST_IMAGE, 1,
                selectedMode);
            break;
        }
      }
    }).create().show();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Logger.e(
        String.format("requestCode = %s, resultCode= %s, data= %s", requestCode, resultCode, data));
    if (resultCode != mActivity.RESULT_OK) return;

    CropEvent cropEvent = new CropEvent();
    cropEvent.setCropMode(CropImageView.CropMode.CIRCLE);
    if (requestCode == REQUEST_IMAGE) {
      ArrayList<String> mSelectPath =
          data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
      cropEvent.setImagePath(mSelectPath.get(0));
      EventBus.getDefault().postSticky(cropEvent);
      skipIntent(CropAct.class, false);
    } else if (requestCode == REQUEST_PORTRAIT_FFC) {//拍照直接返回的
      String path = data.getData() + "";
      Logger.e(String.format("path = %s", path));
      if (!StringUtils.isEmpty(path)) {
        String url = path.substring(path.lastIndexOf("//") + 1);
        cropEvent.setImagePath(url);
        EventBus.getDefault().postSticky(cropEvent);
        skipIntent(CropAct.class, false);
      }
    }
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN) public void onEvent(Bitmap corpBitmap) {
    ivFun.setImageBitmap(corpBitmap);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN) public void onEvent(Uri uri) {
    APP.getInstance().imageLoader.displayImage(uri + "", ivFun, APP.options);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
