package com.nicodelee.beautyarticle.ui.article;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.mode.ActicleMod;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ArticleFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.sc_article)
    ObservableScrollView scActicle;
    @InjectView(R.id.tv_acticle_title)
    TextView tvTitle;
    @InjectView(R.id.tv_acticle_detail)
    TextView tvDetail;

    public static final String EXTRA_POSITION = "ARTICLE_POSITION";
    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(EXTRA_POSITION);
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.inject(this, view);
        EventBus.getDefault().registerSticky(this);
        initView();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        scActicle.setScrollViewCallbacks(this);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }


    public void onEvent(Integer event) {
    }

    public void onEvent(ArrayList<ActicleMod> eventList) {
        tvTitle.setText(eventList.get(position).title + "");
        tvDetail.setText(eventList.get(position).details + "");
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
