package com.nicodelee.beautyarticle.ui.fun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.viewpager.TabletTransformer;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class FunTemplateAct extends BaseSwiBackAct {

  @Bind(R.id.vp_fun_template) ViewPager vpFun;

  private FunTemplateAdt funTemplateAdt;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_fun_template);
    ButterKnife.bind(this);
    initView();
  }

  private void initView(){
    funTemplateAdt = new FunTemplateAdt(getSupportFragmentManager());
    vpFun.setAdapter(funTemplateAdt);
    vpFun.setPageTransformer(true,new TabletTransformer());
  }

  class FunTemplateAdt extends FragmentPagerAdapter{

    public FunTemplateAdt(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      Bundle bundle = new Bundle();
      bundle.putInt(SquareFragment.EXTRA_POSITION,position);
      SquareFragment squareFragment = new SquareFragment();
      squareFragment.setArguments(bundle);
      return squareFragment;
    }

    @Override public int getCount() {
      return 3;
    }
  }




}
