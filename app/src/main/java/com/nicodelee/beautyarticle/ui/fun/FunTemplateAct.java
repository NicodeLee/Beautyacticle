package com.nicodelee.beautyarticle.ui.fun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.beautyarticle.ui.article.ArticleFragment;
import com.nicodelee.viewpager.TabletTransformer;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class FunTemplateAct extends BaseSwiBackAct {

  @Bind(R.id.vp_fun_template) ViewPager vpFun;
  @Bind(R.id.toolbar) protected Toolbar toolbar;

  private FunTemplateAdt funTemplateAdt;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_fun_template);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {

    setSupportActionBar(toolbar);
    final ActionBar ab = getSupportActionBar();
    //ab.setHomeAsUpIndicator(R.drawable.btn_back);
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setTitle("选择模板");

    funTemplateAdt = new FunTemplateAdt(getSupportFragmentManager());
    vpFun.setAdapter(funTemplateAdt);
    vpFun.setPageTransformer(true, new TabletTransformer());
  }

  class FunTemplateAdt extends FragmentPagerAdapter {

    public FunTemplateAdt(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          SquareFragment squareFragment = new SquareFragment();
          return squareFragment;
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
      return 3;
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
