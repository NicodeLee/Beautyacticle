/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicodelee.beautyarticle.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.http.JsonUtil;
import com.nicodelee.beautyarticle.http.URLUtils;
import com.nicodelee.beautyarticle.http.VolleyUtil;
import com.nicodelee.beautyarticle.mode.ActicleList;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.LogUitl;
import com.nicodelee.beautyarticle.viewhelper.MySwipeRefreshLayout;
import com.nicodelee.utils.ListUtils;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheeseListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.recyclerview) RecyclerView rv;
    @InjectView(R.id.swipe_container) MySwipeRefreshLayout mSwipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        ButterKnife.inject(this, view);
        setupRecyclerView(rv);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setRefreshing(true);
        return view;
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        String uri = String.format(URLUtils.ACTITLE+"?order=%1$s&limit=%2$s","-createdAt","10");
        //TODO 使用一段时间再考虑封装
        final JsonObjectRequest request = new JsonObjectRequest(uri, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mSwipeLayout.setRefreshing(false);
                        LogUitl.e("=" + response.toString());
                        ActicleList acticleList = JsonUtil.jsonToMod(response.toString(), ActicleList.class);
                        if (acticleList != null){
                            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),acticleList.results));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeLayout.setRefreshing(false);
                showToast("请求失败:"+error.toString());
            }
        });
        // 请求添加Tag,用于取消请求
        request.setTag(this);
        VolleyUtil.getQueue(getActivity()).add(request);
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


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<ActicleMod> mylist;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @InjectView(R.id.main_title) TextView tvName;
            @InjectView(R.id.main_desc) TextView tvDesc;
            @InjectView(R.id.main_ic) ImageView ivIcon;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
        }


        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<ActicleMod>  items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mylist = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivIcon
                    .getLayoutParams();
            params.width = DevicesUtil.screenWidth;
            params.height = DevicesUtil.screenWidth;
            holder.ivIcon.setLayoutParams(params);

            ActicleMod mod = mylist.get(position);

            holder.tvName.setText(mod.title);
            holder.tvDesc.setText(mod.descriptions);

            APP.getInstance().imageLoader.displayImage(mod.image, holder.ivIcon, APP.options, new SimpleImageLoadingListener());

        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mylist);
        }
    }
}
