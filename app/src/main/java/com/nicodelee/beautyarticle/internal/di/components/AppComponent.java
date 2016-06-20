package com.nicodelee.beautyarticle.internal.di.components;

import android.content.Context;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.app.BaseFragment;
import com.nicodelee.beautyarticle.internal.di.modules.ApplicationModule;
import com.nicodelee.beautyarticle.app.BaseAct;
import com.nicodelee.beautyarticle.navigation.Navigator;
import com.nicodelee.beautyarticle.ui.view.fragment.ActicleListFragment;
import com.nicodelee.beautyarticle.ui.view.activity.MainAct;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by Nicodelee on 15/9/2.
 */
@Singleton @Component(modules = ApplicationModule.class) public interface AppComponent {
  //void inject(MainAct mainAct);
  //void inject(BaseAct baseAct);
  //void inject(BaseFragment baseFragment);
  void inject(APP app);

  //void inject(ActicleListFragment acticleListFragment);

  //Navigator navigator();
  Context context();
}
