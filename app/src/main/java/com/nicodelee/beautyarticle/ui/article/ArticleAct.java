package com.nicodelee.beautyarticle.ui.article;

import android.app.ActionBar;
import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseSwiBackAct;

/**
 * Created by alee on 2015/1/9.
 */
public class ArticleAct extends BaseSwiBackAct{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_article);
        initView();
    }

    private void initView(){
        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.actionbar_bg));
        ab.setDisplayShowHomeEnabled(false);//图标显示
        ab.setDisplayHomeAsUpEnabled(true);//箭头显示
        ab.setHomeButtonEnabled(true);
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
