package com.nicodelee.beautyarticle.ui.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.utils.UILUtils;
import java.util.ArrayList;

public class ArticleFragment extends BaseFragment {

  @Bind(R.id.wv_acticle_detail) WebView webView;
  @Bind(R.id.tv_acticle_detail) TextView tvDetail;
  @Bind(R.id.ic_acticle) ImageView ivActicle;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.fb_share) FloatingActionButton share;

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

  @JavascriptInterface private void initView() {
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setLoadWithOverviewMode(true);//自适应
    webSettings.setUseWideViewPort(true);
    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    webSettings.setAppCacheEnabled(true);
    webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    webView.setHorizontalScrollbarOverlay(false);
    webView.addJavascriptInterface(this, "handler");
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mActivity.finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.fb_share) public void Click(View view) {
    //share
  }

  public void onEvent(ArrayList<ActicleMod> eventList) {
    ActicleMod mod = eventList.get(position);
    collapsingToolbar.setTitle(mod.title + "");
    APP.getInstance().imageLoader.displayImage(mod.image, ivActicle, APP.options,
        new UILUtils.AnimateFirstDisplayListener());
    if (mod.type.equals("Markdown")) {
      webView.setVisibility(View.VISIBLE);
      tvDetail.setVisibility(View.GONE);
      setUpWebView(mod.details);
    } else if (mod.type.equals("text")) {
      webView.setVisibility(View.GONE);
      tvDetail.setVisibility(View.VISIBLE);
      tvDetail.setText(mod.details);
    }
  }

  private void setUpWebView(String mdText) {
    webView.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loadMarkDown(mdText.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n"));
      }
    });
    webView.loadUrl("file:///android_asset/markdown.html");
  }

  private void loadMarkDown(String str) {
    webView.loadUrl("javascript:parseMarkdown(\"" + str + "\")");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
