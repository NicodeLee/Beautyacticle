package com.nicodelee.beautyarticle.http;

import com.loopj.android.http.TextHttpResponseHandler;
import com.nicodelee.beautyarticle.utils.LogUitl;

import org.apache.http.Header;

/**
 * Created by Nicodelee on 15/5/20.
 */
public abstract class AsyncHandlerTextBase extends TextHttpResponseHandler {

    @Override
    public void onStart() {
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String result) {
        LogUitl.e("onSuccess result==" + result);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String result, Throwable throwable) {
        LogUitl.e("onFailure==" + throwable);
    }

}
