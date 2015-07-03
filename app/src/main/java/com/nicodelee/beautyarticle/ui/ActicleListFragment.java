
package com.nicodelee.beautyarticle.ui;

import android.content.Context;
import android.content.Intent;
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

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.http.AsyncHandlerTextBase;
import com.nicodelee.beautyarticle.http.HttpHelper;
import com.nicodelee.beautyarticle.http.JsonUtil;
import com.nicodelee.beautyarticle.http.URLUtils;
import com.nicodelee.beautyarticle.mode.ActicleMainMod;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.article.ArticleAct;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.viewhelper.MySwipeRefreshLayout;
import com.nicodelee.utils.ListUtils;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ActicleListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recyclerview) RecyclerView rv;
    @Bind(R.id.swipe_container) MySwipeRefreshLayout mSwipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheese_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView(rv);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setRefreshing(true);
        return view;
    }


    private void setupRecyclerView(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        new HttpHelper.Builder().toUrl(URLUtils.ACTICLE).executeGet(new AsyncHandlerTextBase() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String result) {
                super.onSuccess(statusCode, headers, result);
                ArrayList<ActicleMainMod> data = JsonUtil.jsonToList(result,ActicleMainMod.class);
                recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),data));
                mSwipeLayout.setRefreshing(false);
            }
        }).build();

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
        private ArrayList<ActicleMainMod> mylist;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.main_title) TextView tvName;
            @Bind(R.id.main_desc) TextView tvDesc;
            @Bind(R.id.main_ic) ImageView ivIcon;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }
        }


        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<ActicleMainMod>  items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mylist = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivIcon
                    .getLayoutParams();
            params.width = DevicesUtil.screenWidth;
            params.height = DevicesUtil.screenWidth;
            holder.ivIcon.setLayoutParams(params);

            final ActicleMod mod = mylist.get(position).fields;

            holder.tvName.setText(mod.title);
            holder.tvDesc.setText(mod.descriptions);

            APP.getInstance().imageLoader.displayImage(mod.image, holder.ivIcon, APP.options, new SimpleImageLoadingListener());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ArticleAct.class);
                    EventBus.getDefault().postSticky(position);
                    EventBus.getDefault().postSticky(mylist);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mylist);
        }
    }
}
