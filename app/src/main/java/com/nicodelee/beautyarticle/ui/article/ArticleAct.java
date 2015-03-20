package com.nicodelee.beautyarticle.ui.article;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by alee on 2015/1/9.
 */
public class ArticleAct extends BaseSwiBackAct implements ObservableScrollViewCallbacks {

    @InjectView(R.id.sc_article) ObservableScrollView scActicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_article);
        ButterKnife.inject(this);
        initView();
    }
    private void initView(){
        initActionBar();
        scActicle.setScrollViewCallbacks(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
