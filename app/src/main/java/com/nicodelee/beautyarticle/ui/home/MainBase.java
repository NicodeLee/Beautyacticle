package com.nicodelee.beautyarticle.ui.home;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.utils.IsExit;

import butterknife.Bind;

/**
 * Created by alee on 2015/7/4.
 */
public abstract class MainBase extends BaseAct {
  @Bind(R.id.toolbar) protected Toolbar toolbar;
  @Bind(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
  @Bind(R.id.nav_view) protected NavigationView navigationView;
  @Bind(R.id.viewpager) protected ViewPager viewPager;
  @Bind(R.id.tabs) protected TabLayout tabLayout;

  // 按返回退出App
  private IsExit exit = new IsExit();

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      pressAgainExit();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  private void pressAgainExit() {
    if (exit.isExit()) {
      finish();
    } else {
      showInfo("喵，再按一次离开^~^");
      exit.doExitInThreeSecond();
    }
  }
}
