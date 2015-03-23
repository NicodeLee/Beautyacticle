package com.nicodelee.beautyarticle.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.nicodelee.beautyarticle.ui.article.ArticleFragment;

public class ArticleAdt extends FragmentStatePagerAdapter {

    public ArticleAdt(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        final Bundle bundle = new Bundle();
        bundle.putInt(ArticleFragment.EXTRA_POSITION, position + 1);
        final ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 20;
    }
}
