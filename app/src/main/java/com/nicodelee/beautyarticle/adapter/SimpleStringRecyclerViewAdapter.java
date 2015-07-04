package com.nicodelee.beautyarticle.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nicodelee.beautyarticle.mode.ActicleMainMod;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.article.ArticleAct;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.utils.ListUtils;
import com.nicodelee.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by alee on 2015/7/4.
 */
public class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
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

        AnimateFirstDisplayListener animateFirstDisplayListener = new AnimateFirstDisplayListener();
        APP.getInstance().imageLoader.displayImage(mod.image, holder.ivIcon, APP.options, animateFirstDisplayListener);

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

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);//动画效果
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mylist);
    }
}
