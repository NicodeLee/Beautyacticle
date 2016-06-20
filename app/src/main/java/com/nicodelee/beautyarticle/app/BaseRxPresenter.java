package com.nicodelee.beautyarticle.app;

import android.os.Bundle;
import nucleus.presenter.RxPresenter;

import static com.nicodelee.beautyarticle.utils.Logger.e;

public class BaseRxPresenter<View> extends RxPresenter<View> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        e("base onCreate");
        //Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        e("base onSave");
        //Icepick.saveInstanceState(this, state);
    }
}