package com.nicodelee.beautyarticle.ui.article;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.adapter.ArticleAdt;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by alee on 2015/1/9.
 */
public class ArticleAct extends BaseSwiBackAct{

    @InjectView(R.id.vp_acticle)ViewPager vpActicle;
    private ArticleAdt mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_article);
        ButterKnife.inject(this);
        initView();
    }
    private void initView(){
        initActionBar();
        mAdapter = new ArticleAdt(getFragmentManager());
        vpActicle.setAdapter(mAdapter);
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

}
