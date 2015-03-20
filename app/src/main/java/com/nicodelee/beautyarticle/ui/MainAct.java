package com.nicodelee.beautyarticle.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.MainAdt;
import com.nicodelee.beautyarticle.adapter.SlidAdt;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.mode.SlidMod;
import com.nicodelee.beautyarticle.ui.article.ArticleAct;
import com.nicodelee.beautyarticle.viewhelper.SlidData;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class MainAct extends BaseAct implements SwipeRefreshLayout.OnRefreshListener {

    //view
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.navdrawer) ListView mDrawerList;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.listview) ObservableListView mListView;
    //click
    @OnItemClick(R.id.listview) void onMainItemClidk(int position){
        skipIntent(ArticleAct.class, false);
    }

    //datehelper
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<SlidMod> slidMods;
    private SlidAdt slidAdt;
    private MainAdt mainAdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        initActionBar();

        mainAdt = new MainAdt(this, slidMods);
        mListView.setAdapter(setBottomInAnimation(mListView, mainAdt));
        mListView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int i, boolean b, boolean b2) {
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                if (scrollState == ScrollState.UP) {
                    if (ab.isShowing()) {
                        ab.hide();
                    }
                } else if (scrollState == ScrollState.DOWN) {
                    if (!ab.isShowing()) {
                        ab.show();
                    }
                }
            }
        });
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.action_bar,
                R.color.action_bar, R.color.action_bar,
                R.color.action_bar);

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //侧滑
        View menuHead = getLayoutInflater().inflate(R.layout.left_menu_head, null);
        mDrawerList.addHeaderView(menuHead);
        slidMods = SlidData.SetData(this);
        slidAdt = new SlidAdt(this, slidMods);
        mDrawerList.setAdapter(slidAdt);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

}