
package com.nicodelee.beautyarticle.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;

import butterknife.ButterKnife;

public class FunFragment extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fun, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
