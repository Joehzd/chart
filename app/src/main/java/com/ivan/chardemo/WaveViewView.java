package com.ivan.chardemo;

/**
 * Created by a123 on 2017/5/16.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by a123 on 2017/5/16.
 */

public class WaveViewView extends View{

    ValueAnimator animator;
    //波浪画笔
    private Paint mPaint;

    private Paint mPaint_two;

    //波浪Path类
    private Path mPath_one;
    //波浪Path类
    private Path mPath_two;

    //一个波浪长度
    private int mWaveLength = 900;
    //波纹个数
    private int mWaveCount;
    //平移偏移量
    private int mOffset;
    //波纹的中间轴
    private int mCenterY;

    //屏幕高度
    private int mScreenHeight;
    //屏幕宽度
    private int mScreenWidth;

    public WaveViewView(Context context) {
        super(context);
    }

    public WaveViewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveViewView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPath_one = new Path();
        mPath_two=new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(new LinearGradient(0,0,0,mScreenHeight,0xFFFFFFFF,0xFFFFFFFF, Shader.TileMode.CLAMP));

        mPaint_two=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_two.setStyle(Paint.Style.FILL);
        mPaint_two.setShader(new LinearGradient(0,0,0,mScreenHeight,0x66FFFFFF,0x66FFFFFF, Shader.TileMode.CLAMP));


        start();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;
        //加1.5：至少保证波纹有2个，至少2个才能实现平移效果
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        mCenterY = mScreenHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath_one.reset();
        mPath_two.reset();
        //移到屏幕外最左边
        mPath_one.moveTo(-mWaveLength + mOffset, mCenterY);
        mPath_two.moveTo(-mWaveLength + mOffset, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            //正弦曲线
            mPath_one.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 60, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath_one.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 60, i * mWaveLength + mOffset, mCenterY);

            mPath_two.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY - 60, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath_two.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY + 60, i * mWaveLength + mOffset, mCenterY);

        }
        //填充矩形

        mPath_one.lineTo(mScreenWidth, mScreenHeight);
        mPath_one.lineTo(0, mScreenHeight);
        mPath_one.close();

        mPath_two.lineTo(mScreenWidth, mScreenHeight);
        mPath_two.lineTo(0, mScreenHeight);
        mPath_two.close();



        canvas.drawPath(mPath_one, mPaint);
        canvas.drawPath(mPath_two,mPaint_two);
    }


    public void start() {
        animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }


    public void pause(){
        if (animator!=null&&animator.isRunning()){
            animator.pause();
        }
    }
    public void resume(){
        if (animator!=null&&animator.isPaused()){
            animator.resume();
        }
    }

    public void stop(){
        if (animator!=null){
            animator.cancel();
        }
    }
}

