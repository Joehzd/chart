package com.ivan.chardemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by a123 on 2017/5/16.
 * surfaceview 无法解决闪屏问题
 */

public class WaveView extends SurfaceView implements SurfaceHolder.Callback {

    //波浪画笔
    private Paint mPaint;
    //测试红点画笔
    private Paint mCyclePaint;
    private SurfaceHolder surfaceHolder;

    //波浪Path类
    //private Path mPath;
    //一个波浪长度
    private int mWaveLength = 1000;
    //波纹个数
    private int mWaveCount;
    //平移偏移量
    private int mOffset = 0;
    //波纹的中间轴
    private int mCenterY;

    //屏幕高度
    private int mScreenHeight;
    //屏幕宽度
    private int mScreenWidth;
    ValueAnimator animator;
    Path mPath;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setZOrderOnTop(true);// 设置置顶（不然实现不了透明）
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mPath = new Path();
        onClick();


    }


    protected void drawLine() {

        int mOffset = 0;
        while (true) {

            clearCanvas();
            long startTime = System.currentTimeMillis();


/*
* 1000ms /60 = 16.67ms 这里，我们采用15，使帧率限制在最大66.7帧
* 如果担心发热、耗电问题，同样可以使用稍大一些的值。经测试80基本为最大值。
*/


            Canvas canvas = surfaceHolder.lockCanvas();
            mPath.reset();
            //移到屏幕外最左边
            mPath.moveTo(-mWaveLength + mOffset, mCenterY);
            for (int i = 0; i < mWaveCount; i++) {
                //正弦曲线
                mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 60, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
                mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 60, i * mWaveLength + mOffset, mCenterY);

            }
            //填充矩形
            mPath.lineTo(mScreenWidth, mScreenHeight);
            mPath.lineTo(0, mScreenHeight);
            mPath.close();
            canvas.drawPath(mPath, mPaint);
            surfaceHolder.unlockCanvasAndPost(canvas);

            mOffset += 8;
            if (mOffset > mWaveLength) {
                mOffset = 0;
            }

            long endTime = System.currentTimeMillis();



            if (endTime - startTime < 1500) {
                try {
                    Thread.sleep(1500 - (endTime - startTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void onClick() {
        animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                System.out.println("offset=" + mOffset);

            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                clearCanvas();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {


            }
        });
        animator.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //onClick();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
//
                drawLine();



            }
        });

        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        mScreenHeight = height;
        mScreenWidth = width;
        //加1.5：至少保证波纹有2个，至少2个才能实现平移效果
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        mCenterY = mScreenHeight / 2;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


    }

    /**
     * 把画布擦干净，准备绘图使用。
     */
    private void clearCanvas() {
        Canvas canvas = surfaceHolder.lockCanvas();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清除画布

        surfaceHolder.unlockCanvasAndPost(canvas);

    }
}
