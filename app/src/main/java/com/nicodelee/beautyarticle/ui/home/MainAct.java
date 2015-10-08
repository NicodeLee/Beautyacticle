package com.nicodelee.beautyarticle.ui.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import butterknife.ButterKnife;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.MainTabPageAdapter;
import com.nicodelee.beautyarticle.ui.fun.FunFragment;
import com.nicodelee.beautyarticle.ui.setting.SettingAct;
import de.greenrobot.event.EventBus;

public class MainAct extends MainBase {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    setSupportActionBar(toolbar);
    final ActionBar ab = getSupportActionBar();
    ab.setHomeAsUpIndicator(R.drawable.ic_menu_grey);
    ab.setDisplayHomeAsUpEnabled(true);
    setupDrawerContent(navigationView);
    setupViewPager(viewPager);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

      boolean isOneSelect = false;

      @Override public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) isOneSelect = true;
        viewPager.setCurrentItem(tab.getPosition());
      }

      @Override public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override public void onTabReselected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0 && !isOneSelect) EventBus.getDefault().postSticky("Reselected");
        isOneSelect = false;
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupViewPager(ViewPager viewPager) {
    MainTabPageAdapter adapter = new MainTabPageAdapter(getSupportFragmentManager());
    adapter.addFragment(new ActicleListFragment(), "文字");
    adapter.addFragment(new FunFragment(), "好玩");
    viewPager.setAdapter(adapter);
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
              case R.id.nav_home:

                break;
              case R.id.nav_setting:
                skipIntent(SettingAct.class, false);
                break;
            }
            return true;
          }
        });
  }
}
