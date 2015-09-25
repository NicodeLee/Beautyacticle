package com.nicodelee.beautyarticle.ui.fun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.app.BaseFragment;

/**
 * Created by Nicodelee on 15/9/25.
 */
public class SquareFragment extends BaseFragment {

  public static final String EXTRA_POSITION = "ARTICLE_POSITION";

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_square, container, false);
    ButterKnife.bind(this, view);
    return view;
  }
}
