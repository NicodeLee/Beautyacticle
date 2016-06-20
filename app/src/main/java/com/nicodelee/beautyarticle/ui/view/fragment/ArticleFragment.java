package com.nicodelee.beautyarticle.ui.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseSupportFragment;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.presenter.ArticleDetailPresenter;
import com.nicodelee.beautyarticle.ui.view.activity.SharePreAct;
import com.nicodelee.beautyarticle.utils.Logger;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.utils.UILUtils;
import com.nicodelee.beautyarticle.viewhelper.LayoutToImage;
import com.nicodelee.beautyarticle.viewhelper.viewtoimage.ViewToImageHelper;
import com.nicodelee.utils.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@RequiresPresenter(ArticleDetailPresenter.class) public class ArticleFragment
    extends BaseSupportFragment<ArticleDetailPresenter> {

  @Bind(R.id.wv_acticle_detail) WebView webView;
  @Bind(R.id.tv_acticle_detail) TextView tvDetail;
  @Bind(R.id.ic_acticle) ImageView ivActicle;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.main_content) CoordinatorLayout mainContent;
  @Bind(R.id.sc_acticle) NestedScrollView scAcrticle;

  public static final String EXTRA_POSITION = "ARTICLE_POSITION";
  private int position;
  private String srcHead;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_article, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    position = getArguments().getInt(EXTRA_POSITION);
    initView();
  }

  private void initView() {
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    initWebView();
  }

  @JavascriptInterface private void initWebView() {
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setLoadWithOverviewMode(true);//自适应
    webSettings.setUseWideViewPort(true);
    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    webSettings.setAppCacheEnabled(true);
    webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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

  @OnClick(R.id.fb_share) public void Click(final View view) {
    //share
    new ViewToImageHelper(new ViewToImageHelper.IViewToImage() {
      @Override
      public void saveToFile(File file, ViewToImageHelper.SaveImageAction saveImageAction) {
        HashMap map = new HashMap();
        map.put("haedpic", srcHead);
        map.put("textPic", "" + file.getAbsoluteFile());
        skipIntent(SharePreAct.class, map, false);
      }
    }).createImageAndShare(mActivity, ViewToImageHelper.SaveImageAction.SHARE, scAcrticle);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onEvent(ArrayList<ActicleMod> eventList) {
    ActicleMod mod = eventList.get(position);
    collapsingToolbar.setTitle(mod.title + "");
    APP.getInstance().imageLoader.displayImage(mod.image, ivActicle, APP.options,
        new UILUtils.AnimateFirstDisplayListener());
    srcHead = mod.image;
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

  private void setUpWebView(final String mdText) {
    if (webView == null) return;
    webView.setWebViewClient(new WebViewClient() {
      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loadMarkDown(mdText.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n"));
      }
    });
    webView.loadUrl("file:///android_asset/markdown.html");
  }

  private void loadMarkDown(String str) {
    if (webView!=null && !StringUtils.isBlank(str))
    webView.loadUrl("javascript:parseMarkdown(\"" + str + "\")");
  }

  @Override protected void injectorPresenter() {
    super.injectorPresenter();
    final PresenterFactory<ArticleDetailPresenter> superFactory = super.getPresenterFactory();
    setPresenterFactory(new PresenterFactory<ArticleDetailPresenter>() {
      @Override public ArticleDetailPresenter createPresenter() {
        ArticleDetailPresenter presenter = superFactory.createPresenter();
        getApiComponent().inject(presenter);
        return presenter;
      }
    });
  }
}
