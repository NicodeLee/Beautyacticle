
package com.nicodelee.beautyarticle.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.michael.easydialog.EasyDialog;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageUtils;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.beautyarticle.viewhelper.VerticalTextView;
import com.nicodelee.utils.WeakHandler;
import com.nicodelee.view.CircularImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

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

    private LayoutToImage layoutToImage;
    private Bitmap bitmap;
    private LayoutInflater inflater;

    private String title,desc;
    private static final int REQUEST_IMAGE = 2;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        inflater  = LayoutInflater.from(mActivity);
        famFun.setClosedOnTouchOutside(true);
        famFun.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override public void onMenuToggle(boolean opened) {
                if (opened){
                    showTip();
                    new WeakHandler().postDelayed(new Runnable() {
                        @Override public void run() {
                         easyDialog.dismiss();
                        }
                    },900);
                }
            }
        });

        tvTitle.setText("一生");
        tvDesc.setTextSize(DevicesUtil.sp2px(mActivity, 18));
        tvDesc.setLineWidth(DevicesUtil.dip2px(mActivity, 30));
        Typeface face = Typeface.createFromAsset(mActivity.getAssets(), "fonts/fun_font.TTF");
        tvDesc.setTypeface(face);
        tvDesc.setText(acticle);
        tvTime.setText(TimeUtils.dateToCnDate(TimeUtils.getCurentData()));
        rlFun.setLayoutParams(new LayoutParams(DevicesUtil.screenWidth, LayoutParams.WRAP_CONTENT));
        layoutToImage = new LayoutToImage(scFun);
    }

    @OnClick({R.id.fb_share,R.id.fb_make,R.id.iv_fun})
    public void Click(View view) {
        switch (view.getId()){
            case R.id.fb_share:
                famFun.close(true);
                bitmap = layoutToImage.convertlayout();
                if (SharImageUtils.saveBitmap(bitmap, SharImageUtils.sharePicName)){
                    ShareHelper.showUp(mActivity);
                }

                break;
            case R.id.fb_make:
                famFun.close(true);
                showEdDialig();
                break;
            case R.id.iv_fun:
                int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                MultiImageSelectorActivity.startSelect(FunFragment.this, REQUEST_IMAGE, 1, selectedMode);
                break;
        }
    }

    private void showEdDialig(){
        View etView = inflater.inflate(R.layout.item_fun_et,null);
        final EditText etTitle = findById(etView,R.id.et_title);
        final EditText etDesc = findById(etView,R.id.et_desc);
        etTitle.setText(title);
        etDesc.setText(desc);

        new AlertDialog.Builder(mActivity)
                .setMessage("发挥你聪明才智")
                .setView(etView)
                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("发表", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        title = etTitle.getText().toString().trim();
                        desc = etDesc.getText().toString().trim();
                        tvTitle.setText(title);
                        tvDesc.setText(desc);
                        dialog.dismiss();
                    }
                })
                .create().show();


        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) etTitle.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etTitle, 0);
            }
        }, 300);

    }

    private EasyDialog easyDialog;
    private void showTip(){
        easyDialog = new EasyDialog(mActivity)
                .setLayoutResourceId(R.layout.layout_tip_text)
                .setBackgroundColor(mActivity.getResources().getColor(R.color.green))
                .setLocationByAttachedView(ivFun)
                .setGravity(EasyDialog.GRAVITY_TOP)
                .setAnimationAlphaShow(600, 0.0f, 1.0f)
                .setAnimationAlphaDismiss(600, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(mActivity.getResources().getColor(R.color.transparent))
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == mActivity.RESULT_OK) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                }

                File imageFile = new File(sb.toString());
                Picasso.with(mActivity)
                        .load(imageFile)
                        .placeholder(me.nereo.multi_image_selector.R.drawable.default_error)
                                //.error(R.drawable.default_error)
                        .resize(300,300)
                        .centerCrop()
                        .into(ivFun);
            }
        }
    }

}
