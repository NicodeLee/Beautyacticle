package com.nicodelee.beautyarticle.internal.di.components;

import android.app.Activity;
import com.nicodelee.beautyarticle.internal.di.PerActivity;
import com.nicodelee.beautyarticle.internal.di.modules.ActivityModule;
import dagger.Component;

/**
 * Created by Nicodelee on 15/9/2.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
  Activity activity();
}
