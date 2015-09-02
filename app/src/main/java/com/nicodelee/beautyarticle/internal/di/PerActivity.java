package com.nicodelee.beautyarticle.internal.di;

import java.lang.annotation.Retention;
import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Nicodelee on 15/9/2.
 */

@Scope @Retention(RUNTIME) public @interface PerActivity {
}
