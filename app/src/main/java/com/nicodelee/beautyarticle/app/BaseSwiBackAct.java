package com.nicodelee.beautyarticle.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nicodelee.view.LoadingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import com.nicodelee.R;


public class BaseSwiBackAct extends SwipeBackActivity {

    public LoadingDialog loadingDialog;
    public Intent intent;
    private static BaseSwiBackAct Cot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cot = this;

        intent = getIntent();

        loadingDialog = new LoadingDialog(this);
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
        Crouton.makeText(this, message, Style.INFO).show();
    }

    public void showErro(String message) {
        Crouton.makeText(this, message, Style.ALERT).show();
    }

    public void showConfirm(String message) {
        Crouton.makeText(this, message, Style.CONFIRM).show();
    }

    public <T> T findViewByIdExt(int id) {
        return (T) super.findViewById(id);
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
//            .showImageOnLoading(R.color.loading_cl)
//            .showImageForEmptyUri(R.color.loading_cl)
////			.showImageOnFail(R.drawable.head_null)
//            .showImageOnFail(R.color.loading_cl)
//            .cacheInMemory(true)
//                    // default
//            .cacheOnDisc(true)
//            .considerExifParams(true)
//            .bitmapConfig(Bitmap.Config.RGB_565)
//                    // .imageScaleType(ImageScaleType.EXACTLY)
////			.showImageForEmptyUri(R.drawable.image_loader_empty)
////			.showImageOnFail(R.drawable.image_loader_fail)
////			.showImageOnLoading(R.drawable.image_loader_loading)
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
