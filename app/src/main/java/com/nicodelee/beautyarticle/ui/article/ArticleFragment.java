package com.nicodelee.beautyarticle.ui.article;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.utils.UILUtils;
import java.util.ArrayList;

public class ArticleFragment extends BaseFragment {

  @Bind(R.id.tv_acticle_detail) TextView tvDetail;
  @Bind(R.id.ic_acticle) ImageView ivActicle;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.toolbar) Toolbar toolbar;

  public static final String EXTRA_POSITION = "ARTICLE_POSITION";
  private int position;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    position = getArguments().getInt(EXTRA_POSITION);
    View view = inflater.inflate(R.layout.fragment_article, container, false);
    ButterKnife.bind(this, view);
    initView();
    return view;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void initView() {
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mActivity.finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void onEvent(ArrayList<ActicleMod> eventList) {
    ActicleMod mod = eventList.get(position);
    collapsingToolbar.setTitle(mod.title + "");
    APP.getInstance().imageLoader.displayImage(mod.image, ivActicle, APP.options,
        new UILUtils.AnimateFirstDisplayListener());
    tvDetail.setText(mod.details);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
