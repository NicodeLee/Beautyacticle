package com.nicodelee.beautyarticle.api;

import com.nicodelee.beautyarticle.mode.ActicleMod;
import java.util.ArrayList;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Nicodelee on 15/8/25.
 */
public interface BeautyApi {
  //@GET("/acticle/{page}/{id}") public void getActicle(@Path("page") int page, @Path("id") int id,
  //    Callback<ArrayList<ActicleMod>> response);
  @GET("/acticle/{page}/{id}") Observable<ArrayList<ActicleMod>> getActicle(@Path("page") int page,
      @Path("id") int id);
}
