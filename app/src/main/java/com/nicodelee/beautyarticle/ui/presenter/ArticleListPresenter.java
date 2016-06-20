package com.nicodelee.beautyarticle.ui.presenter;

import android.os.Bundle;
import com.nicodelee.beautyarticle.api.BeautyApi;
import com.nicodelee.beautyarticle.app.BaseRxPresenter;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.mode.ActicleMod$Table;
import com.nicodelee.beautyarticle.ui.view.fragment.ActicleListFragment;
import com.nicodelee.beautyarticle.utils.Logger;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by NocodeLee on 15/12/8.
 * Email：lirizhilirizhi@163.com
 */
public class ArticleListPresenter extends BaseRxPresenter<ActicleListFragment> {

  private static final int REQUEST_ARCICLE_ID = 1;
  private static final int REQUEST_ARCICLE_LOCAL_ID = 2;

  public static final String WEB = "web";
  public static final String LOCAL = "local";

  //BeautyApi mbeautyApi;
  @Inject BeautyApi mbeautyApi;

  private int page;
  private int id;
  private boolean isSetDate;

  private static final HashMap<String, Integer> requests = new HashMap<>();

  static {
    requests.put(WEB, REQUEST_ARCICLE_ID);
    requests.put(LOCAL, REQUEST_ARCICLE_LOCAL_ID);
  }

  @Override protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);

    Action2<ActicleListFragment, ArrayList<ActicleMod>> onNext =
        new Action2<ActicleListFragment, ArrayList<ActicleMod>>() {
          @Override public void call(ActicleListFragment acticleListFragment,
              ArrayList<ActicleMod> acticleMods) {
            if (!isSetDate) acticleListFragment.onChangeItems(acticleMods, page);
          }
        };

    Action2<ActicleListFragment, Throwable> onError =
        new Action2<ActicleListFragment, Throwable>() {
          @Override public void call(ActicleListFragment acticleListFragment, Throwable throwable) {
            acticleListFragment.onNetworkError(throwable, page);
          }
        };

    restartableLatestCache(REQUEST_ARCICLE_ID, new Func0<Observable<ArrayList<ActicleMod>>>() {
      @Override public Observable<ArrayList<ActicleMod>> call() {
        return mbeautyApi.getActicle(page, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
      }
    }, onNext, onError);

    restartableLatestCache(REQUEST_ARCICLE_LOCAL_ID, new Func0<Observable<List<ActicleMod>>>() {
      @Override public Observable<List<ActicleMod>> call() {
        return Observable.create(new Observable.OnSubscribe<List<ActicleMod>>() {
          @Override public void call(Subscriber<? super List<ActicleMod>> subscriber) {
            List<ActicleMod> acticleMods =
                new Select().from(ActicleMod.class).orderBy(false, ActicleMod$Table.ID).queryList();
            subscriber.onNext(acticleMods);
            subscriber.onCompleted();
          }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }
    }, new Action2<ActicleListFragment, List<ActicleMod>>() {
      @Override
      public void call(ActicleListFragment acticleListFragment, List<ActicleMod> acticleMods) {
        Logger.e("REQUEST_ARCICLE_LOCAL_ID");
        if (!isSetDate) acticleListFragment.setLocalData(acticleMods);
      }
    });
  }

  @Override protected void onSave(Bundle state) {
    super.onSave(state);
    isSetDate = true;
    Logger.e("save in presenter" + "state=" + state.toString());
  }

  //TODO 出来返回会调用上次请求的Next方法
  @Override protected void onTakeView(ActicleListFragment acticleListFragment) {
    super.onTakeView(acticleListFragment);
    Logger.e("onTakeView");
  }

  public void getData(String type, int page, int id) {
    this.page = page;
    this.id = id;
    start(requests.get(type));
  }
}
