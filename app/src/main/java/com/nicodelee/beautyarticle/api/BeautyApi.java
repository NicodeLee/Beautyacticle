package com.nicodelee.beautyarticle.api;

import com.nicodelee.beautyarticle.mode.ActicleMod;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Nicodelee on 15/8/25.
 */
public interface BeautyApi {
  @GET("/acticle/{page}/{id}") public void getActicle(@Path("page") int page, @Path("id") int id,
      Callback<ArrayList<ActicleMod>> response);
}
