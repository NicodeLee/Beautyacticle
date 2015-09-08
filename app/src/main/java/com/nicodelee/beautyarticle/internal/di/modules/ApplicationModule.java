package com.nicodelee.beautyarticle.internal.di.modules;

import android.content.Context;
import com.nicodelee.beautyarticle.api.BeautyApi;
import com.nicodelee.beautyarticle.api.RetrofitHelper;
import com.nicodelee.beautyarticle.app.APP;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by Nicodelee on 15/9/2.
 */
@Module public class ApplicationModule {
  private final APP mapp;

  private BeautyApi mbeautyApi;

  public ApplicationModule(APP app) {
    this.mapp = app;
    mbeautyApi = new RetrofitHelper().getBeautyApi();
  }

  @Provides @Singleton Context provideAppLicationContext() {
    return this.mapp;
  }

  @Provides @Singleton BeautyApi provideBeautyApi() {
    return mbeautyApi;
  }
}
