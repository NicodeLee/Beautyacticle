package com.nicodelee.beautyarticle.ui.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.beautyarticle.ui.view.fragment.RectangleFragment;
import com.nicodelee.beautyarticle.ui.view.fragment.SquareFragment;
import com.nicodelee.viewpager.TabletTransformer;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class FunTemplateAct extends BaseSwiBackAct {

  @Bind(R.id.vp_fun_template) ViewPager vpFun;
  @Bind(R.id.toolbar) protected Toolbar toolbar;
  private static int totalPage = 3;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initView();
  }

  @Override protected int getLayoutResId() {
    return R.layout.act_fun_template;
  }

  private void initView() {

    setSupportActionBar(toolbar);
    final ActionBar ab = getSupportActionBar();
    if (ab!=null){
      ab.setDisplayHomeAsUpEnabled(true);
      ab.setTitle("选择模板");
    }

    FunTemplateAdt funTemplateAdt = new FunTemplateAdt(getSupportFragmentManager());
    vpFun.setAdapter(funTemplateAdt);
    vpFun.setPageTransformer(true, new TabletTransformer());
    vpFun.setOffscreenPageLimit(totalPage);
  }

  class FunTemplateAdt extends FragmentPagerAdapter {

    public FunTemplateAdt(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return new SquareFragment();
        case 1:
          final Bundle bundle = new Bundle();
          bundle.putInt(RectangleFragment.EXTRA_POSITION, position);
          RectangleFragment rectangleFragment = new RectangleFragment();
          rectangleFragment.setArguments(bundle);
          return rectangleFragment;
        case 2:
          final Bundle bundle2 = new Bundle();
          bundle2.putInt(RectangleFragment.EXTRA_POSITION, position);
          RectangleFragment rectangleFragment2 = new RectangleFragment();
          rectangleFragment2.setArguments(bundle2);
          return rectangleFragment2;
      }
      return null;
    }

    @Override public int getCount() {
      return totalPage;
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
