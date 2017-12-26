package com.zz.drawtraining1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/12/20.
 */

public class TimeView1 extends View {
    Paint mPaint, linePaint;
    Path mPath;
    private int count;
    private float len;
    private int MAX;
    private int mWidth;
    private int mHeight;
    private boolean draw;
    private PorterDuffXfermode porterDuffXfermode;
    private PathMeasure pathMeasure;
    private ValueAnimator valueAnimator;
    private float poiTop;
    private float poiBottom;
    private float harf;
    boolean oritation;
    float angle;
    float distance;
    private int space = 10;

    public TimeView1(Context context) {
        super(context);
        init();
    }

    public TimeView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        linePaint = new Paint();
        mPaint.setAntiAlias(true);
        linePaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
        mPath = new Path();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        pathMeasure = new PathMeasure();
        valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                if (oritation) {
                    angle = value;
                } else {
                    distance = value;
                }
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if (oritation) {
                    oritation = false;
//                    valueAnimator.setDuration(20000);
                    valueAnimator.setFloatValues(0, harf);
                } else {
                    oritation = true;
//                    valueAnimator.setDuration(3000);
                    valueAnimator.setFloatValues(0, 180);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
    }

    public void start() {
        if (valueAnimator == null || valueAnimator.isStarted()) return;
        valueAnimator.setFloatValues(0, harf);
        valueAnimator.setDuration(10000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw1(canvas);
        if (draw) {
//            draw0(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        count = 2;
        len = mWidth / count;
        MAX = (int) (len / 5);
        maxH = MAX;
        moveRadio = 2f * len / 30;
        mPath.moveTo(mWidth / 2 + space, mHeight / 2);
        mPath.cubicTo(mWidth, 0, 0, 0, mWidth / 2 - space, mHeight / 2);
        mPath.cubicTo(0, mHeight, mWidth, mHeight, mWidth / 2 + space, mHeight / 2);
        pathMeasure.setPath(mPath, true);
        float top[] = new float[2];
        float bottom[] = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength() / 4, top, null);
        pathMeasure.getPosTan(3 * pathMeasure.getLength() / 4, bottom, null);
        poiTop = top[1];
        poiBottom = bottom[1];
        harf = poiBottom - mHeight / 2;
    }

    int maxH;
    float move;
    float moveRadio;

    private void draw0(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(-2 * len + move, mHeight / 2);
        for (int i = -2; i < count; i++) {
            int rH;
            if (i % 2 == 0)
                rH = mHeight / 2 - maxH;// (int) (mHeight / 2 - Math.random() * MAX);
            else
                rH = mHeight / 2 + maxH; //(int) (mHeight / 2 + Math.random() * MAX);
            float x2 = (i + 1) * len + move;
            float y2 = mHeight / 2;
            float x1 = x2 - len / 2;
            float y1 = rH;
            mPath.quadTo(x1, y1, x2, y2);
        }
        mPath.lineTo(count * len + move, mHeight);
        mPath.lineTo(-2 * len + move, mHeight);
        mPath.lineTo(-2 * len + move, mHeight / 2);
        canvas.drawPath(mPath, mPaint);

        if (move >= 2 * len)
            move = moveRadio;
        else
            move += moveRadio;
        postInvalidateDelayed(100);
    }

    private void draw1(Canvas canvas) {
        if (oritation) {
            canvas.rotate(angle, mWidth / 2, mHeight / 2);
        } else {
            canvas.rotate(0, mWidth / 2, mHeight / 2);
        }
        mPaint.setColor(Color.WHITE);
        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mPath, mPaint);
        mPaint.setColor(0xff0088ff);
        mPaint.setXfermode(porterDuffXfermode);
        canvas.drawRect(0, poiTop + distance, mWidth, mHeight / 2, mPaint);
        canvas.drawRect(0, poiBottom - distance, mWidth
                , 2 * poiBottom - mHeight / 2 - distance, mPaint);
        canvas.restoreToCount(sc);
        mPaint.setXfermode(null);
        canvas.drawRect(mWidth / 2 - space, mHeight / 2, mWidth / 2 + space
                , mHeight / 2 + distance, mPaint);
        canvas.drawPath(mPath, linePaint);
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {

        } else {
        }
    }
}
