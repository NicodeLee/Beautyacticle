package com.nicodelee.beautyarticle.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;


public class APP extends Application {


    public static APP app;

    public static APP getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initImageLoader(getApplicationContext());
        AndroidUtils.init(this);
        DevicesUtil.getScreenConfig(this);
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
                                + "/BeautyArcile/pic")))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }


}
