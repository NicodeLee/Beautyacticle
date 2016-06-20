package com.nicodelee.beautyarticle.ui.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.MainRecyclerViewAdapter;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseSupportFragment;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.ui.presenter.ArticleListPresenter;
import com.nicodelee.beautyarticle.viewhelper.EndlessRecyclerOnScrollListener;
import com.nicodelee.beautyarticle.viewhelper.MySwipeRefreshLayout;
import com.nicodelee.utils.ListUtils;
import com.nicodelee.utils.WeakHandler;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.ArrayList;
import java.util.List;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@RequiresPresenter(ArticleListPresenter.class) public class ActicleListFragment
    extends BaseSupportFragment<ArticleListPresenter>
    implements SwipeRefreshLayout.OnRefreshListener {

  @Bind(R.id.recyclerview) RecyclerView rv;
  @Bind(R.id.swipe_container) MySwipeRefreshLayout mSwipeLayout;

  private List<ActicleMod> macticleMods;
  private MainRecyclerViewAdapter mActcleAdapter;
  private boolean isHasMore = true;
  private LinearLayoutManager linearLayoutManager;

  @Override protected void injectorPresenter() {
    super.injectorPresenter();
    final PresenterFactory<ArticleListPresenter> superFactory = super.getPresenterFactory();
    setPresenterFactory(new PresenterFactory<ArticleListPresenter>() {
      @Override public ArticleListPresenter createPresenter() {
        ArticleListPresenter presenter = superFactory.createPresenter();
        getApiComponent().inject(presenter);
        return presenter;
      }
    });
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main_list, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    macticleMods = new ArrayList<>();
    mSwipeLayout.setOnRefreshListener(this);
    mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent,
        R.color.colorAccent, R.color.colorAccent);
    linearLayoutManager = new LinearLayoutManager(mActivity);
    rv.setLayoutManager(linearLayoutManager);

    if (isInDB()) {
      getPresenter().getData(ArticleListPresenter.LOCAL, 0, 0);
    } else {
      mSwipeLayout.setRefreshing(true);
      getPresenter().getData(ArticleListPresenter.WEB, 0, 0);//首次获取数据
    }
    rv.addOnScrollListener(
        new EndlessRecyclerOnScrollListener(linearLayoutManager, APP.getInstance().imageLoader,
            false, true) {
          @Override public void onLoadMore() {
            int size = macticleMods.size();
            if (isHasMore && !mSwipeLayout.isRefreshing() && size > 0) {
              mSwipeLayout.setRefreshing(true);
              getPresenter().getData(ArticleListPresenter.WEB, 1,
                  (int) macticleMods.get(size - 1).id);
            }
          }
        });
  }

  private boolean isInDB() {
    return new Select().count().from(ActicleMod.class).count() > 0;
  }

  private void setUpData(final int page, final ArrayList<ActicleMod> acticleMods) {
    mSwipeLayout.setRefreshing(false);
    if (ListUtils.isEmpty(acticleMods)) {
      if (page > 0) {
        showInfo("全部加载完毕");
        isHasMore = false;
      } else if (page < 0) {
        showInfo("小编正为你编辑更多文章");
      }
      return;
    }
    //page = 0 首次 <0 刷新 >0 加载更多
    if (page == 0) {
      macticleMods = acticleMods;
      mActcleAdapter = new MainRecyclerViewAdapter(mActivity, macticleMods);
      rv.setAdapter(mActcleAdapter);
    } else if (page > 0) {
      macticleMods.addAll(acticleMods);
      mActcleAdapter.notifyItemInserted(macticleMods.size());
    } else if (page < 0) {
      for (ActicleMod mainMod : acticleMods) {
        macticleMods.add(0, mainMod);
      }
      mActcleAdapter.notifyDataSetChanged();
    }
  }

  private void saveActicles(ArrayList<ActicleMod> acticleMods) {
    for (ActicleMod mainMod : acticleMods) {
      mainMod.save();
    }
  }

  @Override public void onRefresh() {
    new WeakHandler().postDelayed(new Runnable() {
      @Override public void run() {
        if (ListUtils.isEmpty(macticleMods)) {
          getPresenter().getData(ArticleListPresenter.WEB, 0, 0);//first time
        } else {
          getPresenter().getData(ArticleListPresenter.WEB, -1,
              (int) macticleMods.get(0).id);//update
        }
      }
    }, 300);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onEvent(String msg) {
    if (msg.equals("Reselected")) {
      //rv.smoothScrollToPosition(0);
      linearLayoutManager.scrollToPosition(0);
    } else if (msg.equals("clear")) {
      mActcleAdapter.clearData();
    }
  }

  public void onChangeItems(ArrayList<ActicleMod> acticleMods, int pageIndex) {
    saveActicles(acticleMods);
    setUpData(pageIndex, acticleMods);
  }

  public void onNetworkError(Throwable throwable, int pageIndex) {
    mSwipeLayout.setRefreshing(false);
    showInfo("抱歉,出现了一些错:" + throwable.getMessage());
  }

  public void setLocalData(List<ActicleMod> acticleMods) {
    macticleMods = acticleMods;
    mActcleAdapter = new MainRecyclerViewAdapter(mActivity, macticleMods);
    rv.setAdapter(mActcleAdapter);
  }
}
