package com.nicodelee.beautyarticle.internal.di.modules;

import android.app.Activity;
import com.nicodelee.beautyarticle.internal.di.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Nicodelee on 15/9/2.
 */
@Module
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @PerActivity Activity activity() {
    return this.activity;
  }

}

