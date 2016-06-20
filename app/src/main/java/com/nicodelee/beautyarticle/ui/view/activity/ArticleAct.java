package com.nicodelee.beautyarticle.ui.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import butterknife.Bind;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.view.fragment.ArticleFragment;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by alee on 2015/1/9.
 */
public class ArticleAct extends BaseSwiBackAct {

  @Bind(R.id.vp_acticle) ViewPager vpActicle;

  private ArticleAdt mAdapter;
  private int count;
  private int position;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initView();
  }

  @Override protected int getLayoutResId() {
    return R.layout.act_article;
  }

  private void initView() {
    mAdapter = new ArticleAdt(getSupportFragmentManager());
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onEvent(Integer event) {
    position = event;
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onEvent(ArrayList<ActicleMod> eventList) {
    count = eventList.size();
    vpActicle.setAdapter(mAdapter);
    vpActicle.setCurrentItem(position);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  public class ArticleAdt extends FragmentPagerAdapter {

    public ArticleAdt(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override public Fragment getItem(int position) {
      final Bundle bundle = new Bundle();
      bundle.putInt(ArticleFragment.EXTRA_POSITION, position);
      final ArticleFragment fragment = new ArticleFragment();
      fragment.setArguments(bundle);
      return fragment;
    }

    @Override public int getCount() {
      return count;
    }
  }
}
