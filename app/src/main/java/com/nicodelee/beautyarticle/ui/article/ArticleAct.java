package com.nicodelee.beautyarticle.ui.article;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;
import com.nicodelee.beautyarticle.mode.ActicleMod;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by alee on 2015/1/9.
 */
public class ArticleAct extends BaseSwiBackAct {

    @InjectView(R.id.vp_acticle)
    ViewPager vpActicle;

    private ArticleAdt mAdapter;

    ArrayList<ActicleMod> eventList;
    private int count;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_article);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        initActionBar();
        mAdapter = new ArticleAdt(getFragmentManager());
    }


    public void onEvent(Integer event) {
        position = event;
    }

    public void onEvent(ArrayList<ActicleMod> eventList) {
        count = eventList.size();
        vpActicle.setAdapter(mAdapter);
        vpActicle.setCurrentItem(position);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().registerSticky(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ArticleAdt extends FragmentStatePagerAdapter {

        public ArticleAdt(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(ArticleFragment.EXTRA_POSITION, position);
            final ArticleFragment fragment = new ArticleFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }
    }

}
