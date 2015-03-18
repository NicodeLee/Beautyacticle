package com.nicodelee.beautyarticle.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.MainAdt;
import com.nicodelee.beautyarticle.adapter.SlidAdt;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.mode.SlidMod;
import com.nicodelee.beautyarticle.viewhelper.SlidData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends BaseAct implements SwipeRefreshLayout.OnRefreshListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();

    private ArrayList<SlidMod> slidMods;
    private SlidAdt slidAdt;
    private MainAdt mainAdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.actionbar_bg));
        ab.setDisplayShowHomeEnabled(false);//图标显示
        ab.setDisplayHomeAsUpEnabled(true);//箭头显示
        ab.setHomeButtonEnabled(true);

        mListView = (ListView) findViewById(R.id.listview);
        mainAdt = new MainAdt(this,slidMods);
        mListView.setAdapter(mainAdt);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.green,
                R.color.green, R.color.green,
                R.color.green);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);


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
        View menuHead = getLayoutInflater().inflate(R.layout.left_menu_head,null);
        mDrawerList.addHeaderView(menuHead);
        slidMods = SlidData.SetData(this);
        slidAdt = new SlidAdt(this,slidMods);
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
        }, 4000);
    }


}