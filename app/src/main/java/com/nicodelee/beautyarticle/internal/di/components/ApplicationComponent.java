package com.nicodelee.beautyarticle.internal.di.components;

import android.content.Context;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.internal.di.modules.ApplicationModule;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.ui.home.ActicleListFragment;
import com.nicodelee.beautyarticle.ui.home.MainAct;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by Nicodelee on 15/9/2.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseAct baseAct);
  void inject(BaseFragment baseFragment);
  void inject(APP app);
  void inject(MainAct mainAct);
  void inject(ActicleListFragment acticleListFragment);

  Context context();
}
