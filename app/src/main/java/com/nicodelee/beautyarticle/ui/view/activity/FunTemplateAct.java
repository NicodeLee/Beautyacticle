package com.nicodelee.beautyarticle.ui.view.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.jaeger.library.StatusBarUtil;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.beautyarticle.bus.TemplateMenuEvent;
import com.nicodelee.beautyarticle.ui.view.fragment.RectangleFragment;
import com.nicodelee.beautyarticle.ui.view.fragment.SquareFragment;
import com.nicodelee.beautyarticle.ui.view.fragment.VerticalFragment;
import com.nicodelee.beautyarticle.utils.Logger;
import com.nicodelee.beautyarticle.utils.SharImageHelper;
import com.nicodelee.beautyarticle.utils.ShareHelper;
import com.nicodelee.beautyarticle.viewhelper.viewtoimage.ViewToImageHelper;
import java.io.File;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class FunTemplateAct extends BaseSwiBackAct {

  @Bind(R.id.vp_fun_template) HollyViewPager vpFun;
  @Bind(R.id.toolbar) protected Toolbar toolbar;
  private static int totalPage = 3;

  @Override protected int getLayoutResId() {
    return R.layout.act_fun_template;
  }

  @Override protected void initView() {
    setSupportActionBar(toolbar);
    final ActionBar ab = getSupportActionBar();
    if (ab != null) {
      ab.setDisplayHomeAsUpEnabled(true);
      ab.setTitle("卡片画廊");
    }

    int mColor = getResources().getColor(R.color.colorAccent);
    StatusBarUtil.setColor(this, mColor, 0);

    vpFun.getViewPager()
        .setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
    vpFun.setConfigurator(new HollyViewPagerConfigurator() {
      @Override public float getHeightPercentForPage(int page) {
        return ((page + 4) % 10) / 10f;
      }
    });

    FunTemplateAdt funTemplateAdt = new FunTemplateAdt(getSupportFragmentManager());
    vpFun.setAdapter(funTemplateAdt);
    //vpFun.getViewPager().setPageTransformer(true, new TabletTransformer());
    vpFun.getViewPager().setOffscreenPageLimit(totalPage);
  }

  class FunTemplateAdt extends FragmentPagerAdapter {

    public FunTemplateAdt(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return SquareFragment.newInstance(position);
        case 1:
          return RectangleFragment.newInstance(position);
        case 2:
          return VerticalFragment.newInstance(position);
      }
      return null;
    }

    @Override public int getCount() {
      return totalPage;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "简洁";
        case 1:
          return "方正";
        case 2:
          return "优雅";
      }
      return "TITLE " + position;
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.menu_share, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int postion = vpFun.getViewPager().getCurrentItem();
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
      case R.id.menu_share:
        EventBus.getDefault()
            .postSticky(new TemplateMenuEvent(ViewToImageHelper.SaveImageAction.SHARE, postion));
        break;
      case R.id.menu_save:
        EventBus.getDefault()
            .postSticky(
                new TemplateMenuEvent(ViewToImageHelper.SaveImageAction.SAVELOCAL, postion));
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  public void ShareAction(final Activity context, ViewToImageHelper.SaveImageAction saveImageAction,
      View tempalteView) {
    new ViewToImageHelper(new ViewToImageHelper.IViewToImage() {
      @Override
      public void saveToFile(File file, ViewToImageHelper.SaveImageAction saveImageAction) {
        switch (saveImageAction) {
          case SHARE:
            SharImageHelper sharImageHelper = new SharImageHelper();
            Bitmap bitmap =
                APP.getInstance().imageLoader.loadImageSync("file://" + file.getAbsolutePath());
            sharImageHelper.saveBitmap(bitmap, SharImageHelper.sharePicName);
            ShareHelper.showUp(context, sharImageHelper.getShareMod(bitmap));
            break;
          case SAVELOCAL:
            showToast("图片保存路径:" + file.getAbsolutePath());
            break;
        }
      }
    }).createImageAndShare(context, saveImageAction, tempalteView);
  }
}
