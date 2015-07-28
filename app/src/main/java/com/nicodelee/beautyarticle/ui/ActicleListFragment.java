
package com.nicodelee.beautyarticle.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.MainRecyclerViewAdapter;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.http.AsyncHandlerTextBase;
import com.nicodelee.beautyarticle.http.HttpHelper;
import com.nicodelee.beautyarticle.http.JsonUtil;
import com.nicodelee.beautyarticle.http.URLUtils;
import com.nicodelee.beautyarticle.mode.ActicleMainMod;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.mode.ActicleMod$Table;
import com.nicodelee.beautyarticle.viewhelper.MySwipeRefreshLayout;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActicleListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recyclerview) RecyclerView rv;
    @Bind(R.id.swipe_container) MySwipeRefreshLayout mSwipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        ButterKnife.bind(this, view);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setRefreshing(true);
        setupRecyclerView(rv);
        return view;
    }


    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        //请求数据
        if (isInDB(1)){
            mSwipeLayout.setRefreshing(false);
            List<ActicleMod> acticleMods =  new Select().from(ActicleMod.class).queryList();
            recyclerView.setAdapter(getAnimaAdapter(recyclerView,
                    new MainRecyclerViewAdapter(getActivity(),(ArrayList<ActicleMod>)acticleMods)));
        }else {
            new HttpHelper.Builder().toUrl(URLUtils.ACTICLE).executeGet(new AsyncHandlerTextBase() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String result) {
                    super.onSuccess(statusCode, headers, result);
                    mSwipeLayout.setRefreshing(false);
                    ArrayList<ActicleMainMod> data = JsonUtil.jsonToList(result, ActicleMainMod.class);

                    //setDataBase
                    ActicleMod acticleMod;
                    ArrayList<ActicleMod> acticleMods = new ArrayList<ActicleMod>();
                    for (ActicleMainMod mainMod : data) {
                        acticleMod = mainMod.fields;
                        acticleMods.add(mainMod.fields);
                        acticleMod.insert();
                    }

                    recyclerView.setAdapter(getAnimaAdapter(recyclerView,
                            new MainRecyclerViewAdapter(getActivity(), acticleMods)));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String result, Throwable throwable) {
                    mSwipeLayout.setRefreshing(false);
                }
            }).build();
        }


    }

    private boolean isInDB(int index) {
        return new Select()
                .from(ActicleMod.class)
                .where(Condition.column(ActicleMod$Table.ID).eq(index))
                .querySingle() != null;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

}
