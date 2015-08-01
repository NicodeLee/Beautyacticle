package com.nicodelee.beautyarticle.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class APP extends Application {
    private static APP app;
    public static APP getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        app = this;
        initImageLoader(getApplicationContext());
        AndroidUtils.init(this);
        DevicesUtil.getScreenConfig(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCache(
                        new UnlimitedDiscCache(new File(Environment
                                .getExternalStorageDirectory()
                                .getAbsolutePath()
                                + "/Beautyacticle/pic")))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
                .denyCacheImageMultipleSizesInMemory()
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    public ImageLoader imageLoader = ImageLoader.getInstance();
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.loading_cl)
            .showImageForEmptyUri(R.color.loading_cl)
//			.showImageOnFail(R.drawable.head_null)
            .showImageOnFail(R.color.loading_cl)
            .cacheInMemory(true)
                    // default
            .cacheOnDisc(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
                    // .imageScaleType(ImageScaleType.EXACTLY)
//			.showImageForEmptyUri(R.drawable.image_loader_empty)
//			.showImageOnFail(R.drawable.image_loader_fail)
//			.showImageOnLoading(R.drawable.image_loader_loading)
            .displayer(new SimpleBitmapDisplayer()).build();


}
