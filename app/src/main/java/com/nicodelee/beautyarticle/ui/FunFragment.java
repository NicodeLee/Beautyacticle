
package com.nicodelee.beautyarticle.ui;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageUtils;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.beautyarticle.viewhelper.VerticalTextView;
import com.nicodelee.utils.WeakHandler;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

public class FunFragment extends BaseFragment {

    @Bind(R.id.sc_fun) NestedScrollView scFun;
    @Bind(R.id.rl_fun) RelativeLayout rlFun;
    @Bind(R.id.tv_desc) VerticalTextView tvDesc;
    @Bind(R.id.tv_time) TextView tvTime;
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.fl_menu) FloatingActionMenu famFun;

    @BindString(R.string.article) String acticle;

    private LayoutToImage layoutToImage;
    private Bitmap bitmap;
    private LayoutInflater inflater;

    private String sharePicName = "beautyacticle.png";
    private String title,desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        inflater  = LayoutInflater.from(mActivity);
        famFun.setClosedOnTouchOutside(true);

        tvTitle.setText("一生");
        tvDesc.setTextSize(DevicesUtil.sp2px(mActivity, 18));
        tvDesc.setLineWidth(DevicesUtil.dip2px(mActivity, 30));
        Typeface face = Typeface.createFromAsset(mActivity.getAssets(), "fonts/fun_font.TTF");
        tvDesc.setTypeface(face);
        tvDesc.setText(acticle);
        tvTime.setText(TimeUtils.dateToCnDate(TimeUtils.getCurentData()));
        rlFun.setLayoutParams(new LayoutParams(DevicesUtil.screenWidth,LayoutParams.WRAP_CONTENT));

        layoutToImage = new LayoutToImage(scFun);
    }

    @OnClick({R.id.fb_share,R.id.fb_make,R.id.iv_fun})
    public void Click(View view) {
        switch (view.getId()){
            case R.id.fb_share:
                famFun.close(true);
                bitmap = layoutToImage.convertlayout();
                if (SharImageUtils.saveBitmap(bitmap, sharePicName))
                    SharImageUtils.share(AndroidUtils.IMAGE_CACHE_PATH + "/" + sharePicName, "分享", getActivity());
                break;
            case R.id.fb_make:
                famFun.close(true);
                showEdDialig();
                break;
            case R.id.iv_fun:
                showToast("选一张图，说一个故事");
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

}
