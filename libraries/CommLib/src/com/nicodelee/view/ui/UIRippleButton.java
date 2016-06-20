package com.nicodelee.view.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.nicodelee.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lujun on 2015/3/30.
 */
public class UIRippleButton extends UIBaseButton {

    private int mRoundRadius;
    private int mRippleColor;
    private int mRippleDuration;
    private int mRippleRadius;
    private float pointX, pointY;

    private Paint mRipplePaint;
    private RectF mRectF;
    private Path mPath;
    private Timer mTimer;
    private TimerTask mTask;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_DRAW_COMPLETE){
                invalidate();
            }
        }
    };

    private int mRippleAlpha;
    //private final static int HALF_ALPHA = 127;
    private final static int RIPPLR_ALPHE = 47;
    private final static int MSG_DRAW_COMPLETE = 101;

    public UIRippleButton(Context context){
        super(context);
    }

    public UIRippleButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public UIRippleButton(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(final Context context, final AttributeSet attrs){
        super.init(context, attrs);
        if (isInEditMode()){
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIButton);
        mRippleColor = typedArray.getColor(
                R.styleable.UIButton_ripple_color, getResources().getColor(R.color.ripple_color)
        );
        mRippleAlpha = typedArray.getInteger(
                R.styleable.UIButton_ripple_alpha, RIPPLR_ALPHE
        );
        mRippleDuration = typedArray.getInteger(
                R.styleable.UIButton_ripple_duration, 1000
        );
        mShapeType = typedArray.getInt(R.styleable.UIButton_shape_type, 1);
        mRoundRadius = typedArray.getDimensionPixelSize(R.styleable.UIButton_uiradius,
                getResources().getDimensionPixelSize(R.dimen.ui_radius));
        typedArray.recycle();
        mRipplePaint = new Paint();
        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setAlpha(mRippleAlpha);
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRipplePaint.setAntiAlias(true);
        mPath = new Path();
        mRectF = new RectF();
        pointY = pointX = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRipplePaint == null) {
            return;
        }
        drawFillCircle(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            pointX = event.getX();
            pointY = event.getY();
            onStartDrawRipple();
        }
        return super.onTouchEvent(event);
    }

    /** Draw ripple effect*/
    private void drawFillCircle(Canvas canvas){
        if (canvas != null && pointX >= 0 && pointY >= 0){
            int rbX = canvas.getWidth();
            int rbY = canvas.getHeight();
            float longDis = Math.max(pointX, pointY);
            longDis = Math.max(longDis, Math.abs(rbX - pointX));
            longDis = Math.max(longDis, Math.abs(rbY - pointY));
            if (mRippleRadius > longDis) {
                onCompleteDrawRipple();
                return;
            }
            final float drawSpeed = longDis / mRippleDuration * 35;
            mRippleRadius += drawSpeed;

            canvas.save();
//            canvas.translate(0, 0);//保持原点
            mPath.reset();
            canvas.clipPath(mPath);
            if (mShapeType == 0){
                mPath.addCircle(rbX / 2, rbY / 2, WIDTH/2, Path.Direction.CCW);
            }else {
                mRectF.set(0, 0, WIDTH, HEIGHT);
                mPath.addRoundRect(mRectF, mRoundRadius, mRoundRadius, Path.Direction.CCW);
            }
            canvas.clipPath(mPath, Region.Op.REPLACE);
            canvas.drawCircle(pointX, pointY, mRippleRadius, mRipplePaint);
            canvas.restore();
        }
    }

    /** Start draw ripple effect*/
    private void onStartDrawRipple(){
        onCompleteDrawRipple();
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_DRAW_COMPLETE);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 30);
    }

    /** Stop draw ripple effect*/
    private void onCompleteDrawRipple(){
        mHandler.removeMessages(MSG_DRAW_COMPLETE);
        if (mTimer != null){
            if (mTask != null){
                mTask.cancel();
            }
            mTimer.cancel();
        }
        mRippleRadius = 0;
    }
}
