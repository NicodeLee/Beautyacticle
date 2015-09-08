package com.nicodelee.beautyarticle.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Nicodelee on 15/8/25.
 */
public class RetrofitHelper {
  //retrofit http://stackoverflow.com/questions/30201401/retrofit-get-results-in-a-stack-size-error-1036-kb
  private Gson getGson() {
    return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
      @Override public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaredClass().equals(ModelAdapter.class);
      }

      @Override public boolean shouldSkipClass(Class<?> clazz) {
        return false;
      }
    }).create();
  }

  public RestAdapter getRestAdapter() {
    return new RestAdapter.Builder().setConverter(new GsonConverter(getGson()))
        .setEndpoint(URLUtils.BASE_URL)
        .build();
  }

  public BeautyApi getBeautyApi() {
    return new RestAdapter.Builder().setConverter(new GsonConverter(getGson()))
        .setEndpoint(URLUtils.BASE_URL)
        .build().create(BeautyApi.class);
  }
}
