package com.nicodelee.beautyarticle.ui;

import android.os.Bundle;
import android.os.Handler;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseAct;

/**
 * 添加启动页解决先显示系统Actionbar问题
 */
public class WelcomeAct extends BaseAct {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);
        initView();
    }

    private void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skipIntent(MainActNew.class,true);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }

        }, 300);

    }


}
