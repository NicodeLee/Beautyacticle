
package com.nicodelee.beautyarticle.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.SharImageUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FunFragment extends BaseFragment {

    @Bind(R.id.rl_fun) RelativeLayout rlFun;

    private LayoutToImage layoutToImage;
    private Bitmap bitmap;
    private String sharePicName = "beautyacticle.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        layoutToImage = new LayoutToImage(getActivity(), rlFun);
        bitmap = layoutToImage.convertlayout();
    }

    @OnClick({R.id.tv_msg})
    public void Click() {
        if (SharImageUtils.saveBitmap(bitmap, sharePicName))
            SharImageUtils.share(AndroidUtils.IMAGE_CACHE_PATH + "/" + sharePicName, "分享", getActivity());
    }

}
