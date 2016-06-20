package com.nicodelee.beautyarticle.internal.di.components;

import com.nicodelee.beautyarticle.internal.di.modules.ApiModule;
import com.nicodelee.beautyarticle.ui.presenter.ArticleDetailPresenter;
import com.nicodelee.beautyarticle.ui.presenter.ArticleListPresenter;
import com.nicodelee.beautyarticle.ui.view.fragment.ActicleListFragment;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by NocodeLee on 15/12/8.
 * Email：lirizhilirizhi@163.com
 */
@Singleton @Component(modules = ApiModule.class) public interface ApiComponent {
  void inject(ActicleListFragment acticleListFragment);

  void inject(ArticleDetailPresenter articleDetailPresenter);

  void inject(ArticleListPresenter articleListPresenter);
}
