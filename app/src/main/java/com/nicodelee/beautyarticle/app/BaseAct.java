package com.nicodelee.beautyarticle.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.view.LoadingDialog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseAct extends Activity {

    public LoadingDialog loadingDialog;
    public ActionBar ab;
    public Intent intent;
    private static BaseAct Cot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cot = this;
        intent = getIntent();
        loadingDialog = new LoadingDialog(this);
    }

    public void initActionBar() {
        ab = getActionBar();
        ab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.actionbar_bg));
        ab.setDisplayShowHomeEnabled(false);//图标显示
        ab.setDisplayHomeAsUpEnabled(true);//箭头显示
        ab.setHomeButtonEnabled(true);
    }

    public <T> T findViewByIdExt(int id) {
        return (T) super.findViewById(id);
    }

    public SwingBottomInAnimationAdapter setBottomInAnimation(AbsListView absListView,BaseAdapter baseAdapter){
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(baseAdapter);
        swingBottomInAnimationAdapter.setAbsListView(absListView);
        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        return  swingBottomInAnimationAdapter;
    }

    public APP getApp() {
        return (APP) getApplication();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void skipIntent(Class clz, HashMap<String, Object> map,
                           boolean isFinish) {
        Intent intent = new Intent(this, clz);
        if (map != null) {
            Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {

                Map.Entry entry = (Map.Entry) it.next();

                String key = (String) entry.getKey();

                Serializable value = (Serializable) entry.getValue();

                intent.putExtra(key, value);
            }
        }
        startActivity(intent);
        if (isFinish)
            finish();
    }

    public void skipIntent(Class clz, HashMap<String, Object> map, int code) {
        Intent intent = new Intent(this, clz);
        if (map != null) {
            Iterator it = map.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                Serializable value = (Serializable) entry.getValue();
                intent.putExtra(key, value);
            }
        }
        startActivityForResult(intent, code);
    }

    public void skipIntent(Class clz, int code, boolean isFinish) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, code);
        if (isFinish)
            finish();
    }

    public void skipIntent(Class clz, boolean isFinish) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        if (isFinish)
            finish();
    }


    public Object getExtra(String name) {
        return getIntent().getSerializableExtra(name);
    }

//    public ImageLoader imageLoader = ImageLoader.getInstance();
//    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.color.white)
//            .showImageForEmptyUri(R.color.white)
//            .showImageOnFail(R.color.white)
//            .cacheInMemory(true)
//                    // default
//            .cacheOnDisc(true)
//            .considerExifParams(true)
//            .bitmapConfig(Bitmap.Config.RGB_565)
//            .displayer(new SimpleBitmapDisplayer()).build();

    public void showKeyBoard(View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.isActive(v);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // ((EditText)v).setCursorVisible(false);// 失去光标
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        // imm.restartInput(v);
        v.clearFocus();
    }

}
