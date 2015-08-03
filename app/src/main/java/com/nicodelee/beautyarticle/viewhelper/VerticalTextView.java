package com.nicodelee.beautyarticle.viewhelper;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalTextView extends TextView {

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        int m = text.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m; i++) {
            CharSequence index = text.toString().subSequence(i, i + 1);
            sb.append(index + "\n");
        }
        super.setText(sb, type);
    }

}