package com.nicodelee.beautyarticle.service;

import android.app.IntentService;
import android.content.Intent;

import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.mode.ActicleMod$Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

/**
 * Created by Nicodelee on 15/7/28.
 */
public class LoadActicleService extends IntentService{

    public static final String TAG = "LoadActicleService";

    public LoadActicleService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


//    private void loadFrom(int index) {
//        if (index <= 0) {
//            return;
//        }
//        ActicleMod acticleMod;
//        int i;
//        for (i = index; i > index - COUNT_TO_LOAD; i--) {
//            if (!isInDB(i)) {
//                comic = mXkcdRestClient.getApiService().getComic(i);
//                comic.insert();
//            }
//        }
//    }
//
//    private void loadFromLast() {
//        Comic lastComic = mXkcdRestClient.getApiService().getLastComic();
//        if (!isInDB(lastComic.getNum())) {
//            lastComic.insert();
//        }
//        loadFrom(lastComic.getNum() - 1);
//
//    }

    private boolean isInDB(int index) {
        return new Select()
                .from(ActicleMod.class)
                .where(Condition.column(ActicleMod$Table.ID).eq(index))
                .querySingle() != null;
    }
}
