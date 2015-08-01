package com.nicodelee.beautyarticle.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.article.ArticleAct;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.UILUtils;
import com.nicodelee.utils.ListUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by alee on 2015/7/4.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private ArrayList<ActicleMod> mylist;

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


    public MainRecyclerViewAdapter(Context context, ArrayList<ActicleMod> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mylist = items;
    }

    public void setDatas(ArrayList<ActicleMod> acticleMods) {
        if (acticleMods == null) {
            acticleMods = new ArrayList<ActicleMod>();
        } else {
            this.mylist = acticleMods;
        }
        notifyDataSetChanged();
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

        final ActicleMod mod = mylist.get(position);

        holder.tvName.setText(mod.title);
        holder.tvDesc.setText(mod.descriptions);

        APP.getInstance().imageLoader.displayImage(mod.image, holder.ivIcon, APP.options,
                new UILUtils.AnimateFirstDisplayListener());

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
