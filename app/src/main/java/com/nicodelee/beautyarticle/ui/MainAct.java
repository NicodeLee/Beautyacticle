package com.nicodelee.beautyarticle.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.MainAdt;
import com.nicodelee.beautyarticle.adapter.SlidAdt;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.http.JsonUtil;
import com.nicodelee.beautyarticle.http.URLUtils;
import com.nicodelee.beautyarticle.http.VolleyUtil;
import com.nicodelee.beautyarticle.mode.ActicleList;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.mode.SlidMod;
import com.nicodelee.beautyarticle.ui.article.ArticleAct;
import com.nicodelee.beautyarticle.utils.LogUitl;
import com.nicodelee.beautyarticle.viewhelper.SlidData;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class MainAct extends BaseAct implements SwipeRefreshLayout.OnRefreshListener {

    //view
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.navdrawer)
    ListView mDrawerList;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.listview)
    ObservableListView mListView;

    //click
    @OnItemClick(R.id.listview)
    void onMainItemClidk(int position) {
        EventBus.getDefault().postSticky(position);
        EventBus.getDefault().postSticky(acticleList.results);
        skipIntent(ArticleAct.class, false);
    }

    //datehelper
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<SlidMod> slidMods;
    private ArrayList<ActicleMod> acticleMods;
    private SlidAdt slidAdt;
    private MainAdt mainAdt;
    private ActicleList acticleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    public void onEvent(int event) {
    }

    private void initView() {
        initActionBar();

        mainAdt = new MainAdt(this, acticleMods);
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

        //get Date
        //TODO 使用一段时间再考虑封装
        final JsonObjectRequest request = new JsonObjectRequest(URLUtils.ACTITLE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        LogUitl.e("请求数据："+response.toString());
                        acticleList = JsonUtil.jsonToMod(response.toString(), ActicleList.class);
                        if (acticleList != null)
                            mainAdt.setAdtList(acticleList.results);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("请求失败");
            }
        });

        // 请求添加Tag,用于取消请求
//        request.setShouldCache(true);
        request.setTag(this);
        VolleyUtil.getQueue(this).add(request);

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

    @Override
    protected void onDestroy() {
        VolleyUtil.getQueue(this).cancelAll(this);
        super.onDestroy();
    }
}