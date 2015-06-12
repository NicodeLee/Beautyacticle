package com.nicodelee.beautyarticle.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.view.LoadingDialog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseAct extends AppCompatActivity {

    public LoadingDialog loadingDialog;
    public Intent intent;
    private static BaseAct Cot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cot = this;
        intent = getIntent();
        loadingDialog = new LoadingDialog(this);
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

    public void showInfo(String message) {
        AppMsg.Style style = new AppMsg.Style(1500, R.color.accent);
        AppMsg appMsg = AppMsg.makeText(this, message, style);
        appMsg.setAnimation(R.anim.slide_in_top,R.anim.slide_out_bottom);
        appMsg.show();
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

}
