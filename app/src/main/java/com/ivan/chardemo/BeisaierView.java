package com.ivan.chardemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by ivan on 2017/5/7.
 */

public class BeisaierView extends View {


    Paint paint;
    Path path;
    int width,height,centerY,count,waveLength=1000;
    int offset;
    ValueAnimator valueAnimator;

    public BeisaierView(Context context) {
        super(context);
    }

    public BeisaierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        path=new Path();
        animators();
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=MeasureSpec.getSize(widthMeasureSpec);
        height=MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        centerY=h/2;
        waveLength=width;
        count=width/waveLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.reset();
        path.moveTo(-waveLength,centerY);

        for (int i=0;i<count+5;i++){
            path.quadTo(-waveLength*3/4+i*waveLength+offset,centerY-100,-waveLength/2+i*waveLength+offset,centerY);
            path.quadTo(-waveLength/4+i*waveLength+offset,centerY+100,i*waveLength+offset,centerY);
        }
        //
        canvas.drawPath(path,paint);
    }
    private void animators(){
        valueAnimator=ValueAnimator.ofInt(0,waveLength);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset= (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                valueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                valueAnimator.start();
            }
        });
    }
}
