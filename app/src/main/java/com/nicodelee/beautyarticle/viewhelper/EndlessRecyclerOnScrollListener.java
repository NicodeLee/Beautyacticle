package com.nicodelee.beautyarticle.viewhelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
  public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

  private int previousTotal = 0; // The total number of items in the dataset after the last load
  private boolean loading = true; // True if we are still waiting for the last set of data to load.
  private int visibleThreshold = 1;
  // The minimum amount of items to have below your current scroll position before loading more.
  int firstVisibleItem, visibleItemCount, totalItemCount;

  //    private int current_page = 1;
  private LinearLayoutManager mLinearLayoutManager;

  private ImageLoader imageLoader;
  private final boolean pauseOnScroll;
  private final boolean pauseOnSettling;
  private final RecyclerView.OnScrollListener externalListener;
  private boolean stopped = false;

  public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager,
      ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnSettling) {
    this(linearLayoutManager, imageLoader, pauseOnScroll, pauseOnSettling, null);
    this.mLinearLayoutManager = linearLayoutManager;
  }

  public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager,
      ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnSettling,
      RecyclerView.OnScrollListener customListener) {
    this.imageLoader = imageLoader;
    this.pauseOnScroll = pauseOnScroll;
    this.pauseOnSettling = pauseOnSettling;
    externalListener = customListener;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    //super.onScrolled(recyclerView, dx, dy);
    if (externalListener != null) {
      externalListener.onScrolled(recyclerView, dx, dy);
    }

    if (mLinearLayoutManager == null) return;


    visibleItemCount = recyclerView.getChildCount();
    totalItemCount = mLinearLayoutManager.getItemCount();
    firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false;
        previousTotal = totalItemCount;
      }
    }
    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
      // End has been reached
      // Do something
      // current_page++;

      onLoadMore();

      loading = true;
    }

  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    switch (newState) {
      case RecyclerView.SCROLL_STATE_IDLE:
        imageLoader.resume();
        stopped = false;
        break;
      case RecyclerView.SCROLL_STATE_DRAGGING:
        if (pauseOnScroll) {
          imageLoader.pause();
          stopped = true;
        } else if (stopped) {
          imageLoader.resume();
          stopped = false;
        }
        break;
      case RecyclerView.SCROLL_STATE_SETTLING:
        if (pauseOnSettling) {
          imageLoader.pause();
          stopped = true;
        } else if (stopped) {
          imageLoader.resume();
          stopped = false;
        }
        break;
    }
    if (externalListener != null) {
      externalListener.onScrollStateChanged(recyclerView, newState);
    }
  }

  public abstract void onLoadMore();

  public void reset(int previousTotal, boolean loading) {
    this.previousTotal = previousTotal;
    this.loading = loading;
  }
}