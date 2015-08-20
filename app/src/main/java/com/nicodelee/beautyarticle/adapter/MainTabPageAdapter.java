package com.nicodelee.beautyarticle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alee on 2015/7/4.
 */
public class MainTabPageAdapter extends FragmentPagerAdapter {
  private final List<Fragment> mFragments = new ArrayList<>();
  private final List<String> mFragmentTitles = new ArrayList<>();

  public MainTabPageAdapter(FragmentManager fm) {
    super(fm);
  }

  public void addFragment(Fragment fragment, String title) {
    mFragments.add(fragment);
    mFragmentTitles.add(title);
  }

  @Override public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override public int getCount() {
    return mFragments.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    return mFragmentTitles.get(position);
  }
}
