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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.article.ArticleAct;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.utils.L;
import com.nicodelee.utils.ListUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alee on 2015/7/4.
 */
public class MainRecyclerViewAdapter
    extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

  private final TypedValue mTypedValue = new TypedValue();
  private List<ActicleMod> mylist;

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.main_title) TextView tvName;
    @Bind(R.id.main_desc) TextView tvDesc;
    @Bind(R.id.main_ic) ImageView ivIcon;
    @Bind(R.id.rl_msg) RelativeLayout rlMsg;
    @Bind(R.id.number_progress_bar) NumberProgressBar progressBar;
    public final View mView;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      ButterKnife.bind(this, view);
    }

    private Observable<Bitmap> loadBitmap(String url) {
      return Observable.create(subscriber -> {
        APP.getInstance().imageLoader.displayImage(url, ivIcon, APP.options,
            new ImageLoadingListener() {
              final List<String> displayedImages =
                  Collections.synchronizedList(new LinkedList<String>());

              @Override public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
              }

              @Override
              public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                subscriber.onError(failReason.getCause());
              }

              @Override
              public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                subscriber.onNext(loadedImage);
                subscriber.onCompleted();
              }

              @Override public void onLoadingCancelled(String imageUri, View view) {
                subscriber.onError(new Throwable("Image loading cancelled"));
              }
            });
      });
    }
  }

  public MainRecyclerViewAdapter(Context context, List<ActicleMod> items) {
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

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {

    RelativeLayout.LayoutParams params =
        (RelativeLayout.LayoutParams) holder.ivIcon.getLayoutParams();
    params.width = DevicesUtil.screenWidth;
    params.height = DevicesUtil.screenWidth;
    holder.ivIcon.setLayoutParams(params);

    final ActicleMod mod = mylist.get(position);

    holder.tvName.setText(mod.title);
    holder.tvDesc.setText(mod.descriptions);

    Observable.create(new Observable.OnSubscribe<Bitmap>() {
      @Override public void call(Subscriber<? super Bitmap> subscriber) {
        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        APP.getInstance().imageLoader.displayImage(mod.image, holder.ivIcon, APP.options,
            new ImageLoadingListener() {
              @Override public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setProgress(0);
                holder.progressBar.setVisibility(View.VISIBLE);
              }

              @Override
              public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                subscriber.onError(failReason.getCause());
                holder.progressBar.setVisibility(View.GONE);
              }

              @Override
              public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                subscriber.onNext(loadedImage);
                subscriber.onCompleted();
                holder.progressBar.setVisibility(View.GONE);
                if (loadedImage != null) {
                  ImageView imageView = (ImageView) view;
                  boolean firstDisplay = !displayedImages.contains(imageUri);
                  if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);//动画效果
                    displayedImages.add(imageUri);
                  }
                }
              }

              @Override public void onLoadingCancelled(String imageUri, View view) {
                subscriber.onError(new Throwable("Image loading cancelled"));
              }
            }, new ImageLoadingProgressListener() {
              @Override
              public void onProgressUpdate(String imageUri, View view, int current, int total) {
                holder.progressBar.setProgress(Math.round(100.0f * current / total));
              }
            });
      }
    }).observeOn(AndroidSchedulers.mainThread())
        //.observeOn(Schedulers.io())
        .subscribe(new Observer<Bitmap>() {
          @Override public void onCompleted() {
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onNext(Bitmap bitmap) {
            holder.ivIcon.setImageBitmap(bitmap);
          }
        });

    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Context context = v.getContext();
        Intent intent = new Intent(context, ArticleAct.class);
        EventBus.getDefault().postSticky(position);
        EventBus.getDefault().postSticky(mylist);
        context.startActivity(intent);
      }
    });
  }

  @Override public int getItemCount() {
    return ListUtils.getSize(mylist);
  }

  public void clearData() {
    int size = this.mylist.size();
    if (size > 0) {
      for (int i = 0; i < size; i++) {
        this.mylist.remove(0);
      }

      this.notifyItemRangeRemoved(0, size);
    }
  }
}
