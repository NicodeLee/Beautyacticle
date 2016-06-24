package com.nicodelee.beautyarticle.ui.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.bus.TemplateMenuEvent;
import com.nicodelee.beautyarticle.ui.view.activity.CameraActivity;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.viewhelper.VerticalTextView;
import com.nicodelee.utils.WeakHandler;
import java.io.File;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import org.greenrobot.eventbus.EventBus;

import static butterknife.ButterKnife.findById;

/**
 * Created by Nicodelee on 15/9/28.
 */
public class TemplateVerticalBase extends BaseFragment {

  @Bind(R.id.tv_fun) VerticalTextView tvDesc;
  @Bind(R.id.tv_by) TextView tvBy;

  protected static final int REQUEST_IMAGE = 2;
  private static final int REQUEST_PORTRAIT_FFC = 3;

  protected LayoutInflater inflater;
  protected String title, desc;
  @BindString(R.string.article) String acticle;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  protected void showEdDialig(boolean isShowTitle) {
    View etView = inflater.inflate(R.layout.item_fun_et, null);
    final EditText etDesc = findById(etView, R.id.et_desc);
    final EditText etTitle = findById(etView, R.id.et_title);
    final TextInputLayout textInputLayout = findById(etView, R.id.textInput_title);
    if (isShowTitle) {
      etTitle.setVisibility(View.VISIBLE);
      textInputLayout.setHint("By 某人");
    } else {
      etTitle.setVisibility(View.GONE);
    }
    etDesc.setText(desc);
    etTitle.setText(title);

    new AlertDialog.Builder(mActivity).setMessage("发挥想象")
        .setView(etView)
        .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .setPositiveButton("发表", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {

            desc = etDesc.getText().toString().trim();
            title = etTitle.getText().toString().trim();
            if (!TextUtils.isEmpty(desc)) {
              tvDesc.setText(desc);
            }
            if (!TextUtils.isEmpty(title)) {
              tvBy.setText(title);
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

  protected void showChiocePicDialog() {
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
            MultiImageSelectorActivity.startSelect(getActivity(), REQUEST_IMAGE, 1, selectedMode);
            break;
        }
      }
    }).create().show();
  }

  public void removeStickEven() {
    TemplateMenuEvent stickEvent = EventBus.getDefault().getStickyEvent(TemplateMenuEvent.class);
    if (stickEvent != null) {
      EventBus.getDefault().removeStickyEvent(stickEvent);
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
