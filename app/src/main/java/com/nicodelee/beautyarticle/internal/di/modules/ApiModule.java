package com.nicodelee.beautyarticle.internal.di.modules;

import com.nicodelee.beautyarticle.api.BeautyApi;
import com.nicodelee.beautyarticle.api.RetrofitHelper;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by NocodeLee on 15/12/8.
 * Email：lirizhilirizhi@163.com
 */
@Module(includes = ApplicationModule.class) public class ApiModule {

  @Provides @Singleton BeautyApi provideBeautyApi() {
    return new RetrofitHelper().getBeautyApi();
  }
}
