package com.nicodelee.view.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import com.nicodelee.R;

/**
 * Created by drakeet on 4/2/15.
 */
public class UIBaseButton extends Button {

    protected int WIDTH;
    protected int HEIGHT;
    protected Paint mBackgroundPaint;
    protected int mShapeType;
    protected int mRadius;

    public UIBaseButton(Context context) {
        this(context, null);
    }

    public UIBaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UIBaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UIBaseButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode()) return;
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIButton);
        mShapeType = typedArray.getInt(R.styleable.UIButton_shape_type, 1);
        mRadius = typedArray.getDimensionPixelSize(
            R.styleable.UIButton_uiradius, getResources().getDimensionPixelSize(R.dimen.ui_radius));
        int unpressedColor = typedArray.getColor(R.styleable.UIButton_color_unpressed, Color.TRANSPARENT);
        typedArray.recycle();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAlpha(Color.alpha(unpressedColor));
        mBackgroundPaint.setColor(unpressedColor);
        mBackgroundPaint.setAntiAlias(true);

        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
        if (unpressedColor != Color.TRANSPARENT)
            this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        WIDTH = w;
        HEIGHT = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBackgroundPaint == null) {
            super.onDraw(canvas);
            return;
        }
        if (mShapeType == 0) {
            canvas.drawCircle(WIDTH / 2, HEIGHT / 2, WIDTH / 2, mBackgroundPaint);
        } else {
            RectF rectF = new RectF();
            rectF.set(0, 0, WIDTH, HEIGHT);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mBackgroundPaint);
        }
        super.onDraw(canvas);
    }
}
