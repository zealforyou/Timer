package com.zz.drawtraining1;

import android.animation.Animator;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/20.
 */

public class TimeView extends View implements ValueAnimator.AnimatorUpdateListener, Animator
        .AnimatorListener {
    private float MIN_OPEN_SIZE = 1;
    Paint mPaint, linePaint;
    Path mPath;
    private int mWidth;
    private int mHeight;
    private PorterDuffXfermode porterDuffXfermode;
    private PathMeasure pathMeasure;
    private ValueAnimator topAnimator;
    private ValueAnimator oritationAnimator;
    private ValueAnimator ioAnimator;
    private float poiTop;
    private float poiBottom;
    private float harf;
    boolean oritation;
    float angle;
    float distance;
    float bttomDistance;
    float ioDistance;
    private float space = 3;
    private boolean ioAnimatorEnd;
    private ValueAnimator bottomAnimator;
    private boolean init;

    public TimeView(Context context) {
        super(context);
        init();
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    }

    private void initListener() {
        topAnimator = new ValueAnimator();
        bottomAnimator = new ValueAnimator();
        ioAnimator = new ValueAnimator();
        oritationAnimator = new ValueAnimator();
        topAnimator.setInterpolator(new LinearInterpolator());
        bottomAnimator.setInterpolator(new LinearInterpolator());
        ioAnimator.setInterpolator(new AccelerateInterpolator());
        ioAnimator.setDuration(500);
        oritationAnimator.setDuration(3000);
        oritationAnimator.setFloatValues(0, 180);
        topAnimator.addUpdateListener(this);
        bottomAnimator.addUpdateListener(this);
        ioAnimator.addUpdateListener(this);
        oritationAnimator.addUpdateListener(this);
        topAnimator.addListener(this);
        bottomAnimator.addListener(this);
        ioAnimator.addListener(this);
        oritationAnimator.addListener(this);
        init = true;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation == topAnimator) {
            oritation = true;
            oritationAnimator.start();
        } else if (animation == oritationAnimator) {
            oritation = false;
            ioAnimatorEnd = false;
            topAnimator.start();
            ioAnimator.start();
        } else if (animation == ioAnimator) {
            ioAnimatorEnd = true;
            bottomAnimator.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Float value = (Float) animation.getAnimatedValue();
        if (animation == topAnimator) {
            distance = value;
        } else if (animation == bottomAnimator) {
            bttomDistance = value;
        } else if (animation == oritationAnimator) {
            angle = value;
        } else if (animation == ioAnimator) {
            ioDistance = value;
        }
        invalidate();
    }

    public void start(int SECOND) {
        if (SECOND / 3600 > 3) {
            Toast.makeText(getContext(), "最大支持三个小时", Toast.LENGTH_SHORT).show();
            return;
        }
        if (init) {
            stop();
        }
        initListener();
        topAnimator.setFloatValues(0, harf);
        ioAnimator.setFloatValues(0, harf);
        bottomAnimator.setFloatValues(0, harf);
        topAnimator.setDuration(SECOND * 1000 + 500);
        bottomAnimator.setDuration(SECOND * 1000);
        topAnimator.start();
        ioAnimator.start();
    }

    private void reset() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw1(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
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
        if (ioAnimatorEnd) {
            canvas.drawRect(0, poiBottom - bttomDistance, mWidth
                    , 2 * poiBottom - mHeight / 2 - bttomDistance, mPaint);
        }
        canvas.restoreToCount(sc);
        mPaint.setXfermode(null);
        canvas.drawRect(mWidth / 2 - space, mHeight / 2
                , mWidth / 2 + space
                , mHeight / 2 + ioDistance, mPaint);
        canvas.drawPath(mPath, linePaint);
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {

        } else {
            stop();
        }
    }

    public void stop() {
        oritation = false;
        angle = 0;
        distance = 0;
        bttomDistance = 0;
        ioDistance = 0;
        ioAnimatorEnd = false;
        if (bottomAnimator != null) {
            if (bottomAnimator.isStarted()) {
                bottomAnimator.cancel();
            }
            bottomAnimator = null;
        }
        if (topAnimator != null) {
            if (topAnimator.isStarted()) {
                topAnimator.cancel();
            }
            topAnimator = null;
        }
        if (ioAnimator != null) {
            if (ioAnimator.isStarted()) {
                ioAnimator.cancel();
            }
            ioAnimator = null;
        }
    }


}
