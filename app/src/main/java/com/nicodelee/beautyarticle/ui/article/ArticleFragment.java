package com.nicodelee.beautyarticle.ui.article;

import android.app.ActionBar;
import android.os.Bundle;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticleFragment extends BaseFragment implements ObservableScrollViewCallbacks {
    public static final String EXTRA_POSITION = "ARTICLE_POSITION";
    @InjectView(R.id.sc_article)ObservableScrollView scActicle;
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_article);
        ButterKnife.inject(this,getContentView());
        scActicle.setScrollViewCallbacks(this);
    }
    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
    }

    @Override
    public void onDownMotionEvent() {
    }
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getActivity().getActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
