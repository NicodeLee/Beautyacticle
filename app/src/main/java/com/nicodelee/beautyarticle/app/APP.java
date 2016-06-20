package com.nicodelee.beautyarticle.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.internal.di.components.ApiComponent;
import com.nicodelee.beautyarticle.internal.di.components.AppComponent;
import com.nicodelee.beautyarticle.internal.di.components.DaggerApiComponent;
import com.nicodelee.beautyarticle.internal.di.components.DaggerAppComponent;
import com.nicodelee.beautyarticle.internal.di.modules.ApplicationModule;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.beautyarticle.viewhelper.OkHttpImageDownloader;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.raizlabs.android.dbflow.config.FlowManager;
import java.io.File;
import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class APP extends Application {

  private static APP app;

  public static APP getInstance() {
    return app;
  }

  //private RefWatcher refWatcher;

  private AppComponent applicationComponent;
  private ApiComponent apiComponent;

  @Override public void onCreate() {
    super.onCreate();
    initialzeInjector();
    initApiialzeInjector();
    FlowManager.init(this);//DbFlow
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-Light.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
    app = this;

    //refWatcher = LeakCanary.install(this);

    AndroidUtils.init(this);
    DevicesUtil.getScreenConfig(this);
    initImageLoader(getApplicationContext());
  }

  //public static RefWatcher getRefWatcher(Context context) {
  //  return app.refWatcher;
  //}

  public AppComponent getApplicationComponent() {
    return applicationComponent;
  }

  public ApiComponent getApiComponent() {
    return apiComponent;
  }

  public static APP from(@NonNull Context context) {
    return (APP) context.getApplicationContext();
  }

  private void initialzeInjector() {
    this.applicationComponent =
        DaggerAppComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    //applicationComponent.inject(this);
  }

  private void initApiialzeInjector() {
    this.apiComponent =
        DaggerApiComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
  }
  private void initImageLoader(Context context) {
    ImageLoaderConfiguration.Builder config =
        new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
            .denyCacheImageMultipleSizesInMemory()
            .diskCacheFileNameGenerator(new Md5FileNameGenerator())
            .diskCacheSize(50 * 1024 * 1024)
            .imageDownloader(new OkHttpImageDownloader(context, new OkHttpClient()))
            .diskCache(new UnlimitedDiskCache(new File(AndroidUtils.IMAGE_CACHE_PATH)))
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .diskCacheFileCount(200)
            .writeDebugLogs();

    ImageLoader.getInstance().init(config.build());
  }

  public ImageLoader imageLoader = ImageLoader.getInstance();
  public static DisplayImageOptions options =
      new DisplayImageOptions.Builder().showImageOnLoading(R.color.loading_cl)
          .showImageForEmptyUri(R.color.loading_cl)
          .showImageOnFail(R.color.loading_cl)
          .cacheInMemory(true)
          .cacheOnDisk(true)
          .considerExifParams(true)
          .bitmapConfig(Bitmap.Config.RGB_565)
          // .imageScaleType(ImageScaleType.EXACTLY)
          //			.showImageForEmptyUri(R.drawable.image_loader_empty)
          //			.showImageOnFail(R.drawable.image_loader_fail)
          //			.showImageOnLoading(R.drawable.image_loader_loading)
          .displayer(new SimpleBitmapDisplayer())
          .build();
}
