package com.nicodelee.beautyarticle.internal.di.components;

import android.content.Context;
import com.nicodelee.beautyarticle.internal.di.modules.ApplicationModule;
import com.nicodelee.beautyarticle.app.BaseAct;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by Nicodelee on 15/9/2.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseAct baseAct);

  Context context();
}
