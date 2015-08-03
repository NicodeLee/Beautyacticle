
package com.nicodelee.beautyarticle.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.SharImageUtils;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.beautyarticle.viewhelper.VerticalTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FunFragment extends BaseFragment {

    @Bind(R.id.sc_fun) ScrollView scFun;
    @Bind(R.id.rl_fun) RelativeLayout rlFun;
    @Bind(R.id.tv_desc) VerticalTextView tvDesc;
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_time) TextView tvTime;

    private LayoutToImage layoutToImage;
    private Bitmap bitmap;
    private String sharePicName = "beautyacticle.png";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        tvTitle.setText("一生");
        tvDesc.setText("我行过许多地方的桥");
        tvTime.setText(TimeUtils.dateToCnDate(TimeUtils.getCurentData()));
        rlFun.setLayoutParams(new LayoutParams(DevicesUtil.screenWidth,LayoutParams.MATCH_PARENT));
        layoutToImage = new LayoutToImage(scFun);
        bitmap = layoutToImage.convertlayout();
    }

    @OnClick({R.id.rl_fun})
    public void Click() {
        if (SharImageUtils.saveBitmap(bitmap, sharePicName))
            SharImageUtils.share(AndroidUtils.IMAGE_CACHE_PATH + "/" + sharePicName, "分享", getActivity());
    }

}
