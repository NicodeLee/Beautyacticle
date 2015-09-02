package com.nicodelee.beautyarticle.internal.di.modules;

import android.content.Context;
import com.nicodelee.beautyarticle.app.APP;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by Nicodelee on 15/9/2.
 */
@Module public class ApplicationModule {
  private final APP app;

  public ApplicationModule(APP app) {
    this.app = app;
  }

  @Provides @Singleton Context provideAppLicationContext() {
    return this.app;
  }

}
