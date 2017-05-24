package com.ivan.chardemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by ivan on 2017/5/23.
 */

public class ScanView extends View {
    Drawable drawable;
    Drawable drawable2;

    Paint paint,cleanPaint,tempPaint;
    int mScreenWidth,mScreenHeight,mWidth;
    int changeWidth,changeHeight;
    Bitmap bitmap_cpu_cover;
    Bitmap bitmap_cpu_line;
    Bitmap bitmap_scan_line_top,bitmap_scan_line_bottom;
    RadialGradient radialGradient;
    public ScanView(Context context) {
        super(context);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScanView);
        drawable = typedArray.getDrawable(R.styleable.ScanView_first_bg);
        drawable2 = typedArray.getDrawable(R.styleable.ScanView_second_bg);
        typedArray.recycle();
        mScreenWidth=context.getResources().getDisplayMetrics().widthPixels;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);


        tempPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#FFFfff"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        cleanPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        cleanPaint.setColor(Color.argb(255,255,255,255));
        cleanPaint.setStyle(Paint.Style.STROKE);
        cleanPaint.setStrokeWidth(5);

        cleanPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        cleanPaint.setDither(true);




        tempPaint.setColor(Color.argb(0,0,0,0));
        tempPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));


        bitmap_cpu_cover= BitmapFactory.decodeResource(getResources(),R.drawable.cpu_cover_bg);
        bitmap_cpu_line= BitmapFactory.decodeResource(getResources(),R.drawable.cpu_line_bg);

        bitmap_scan_line_top=BitmapFactory.decodeResource(getResources(),R.drawable.ic_scan_out_oval_top);
        bitmap_scan_line_bottom=BitmapFactory.decodeResource(getResources(),R.drawable.ic_scan_out_oval_bottom);
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(1200);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                changeHeight= (int) (mScreenHeight*value);
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

                System.out.println("我在重复");
                Bitmap temp=null;
                Drawable tem=null;
                temp=bitmap_cpu_cover;
                bitmap_cpu_cover=bitmap_cpu_line;
                bitmap_cpu_line=temp;
//                tem=drawable;
//                drawable=drawable2;
//                drawable2=tem;

            }
        });
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    private int getSize(int defaultValue,int measureSpec){
        int tempSize=defaultValue;

        int mode=MeasureSpec.getMode(measureSpec);
        int size=MeasureSpec.getSize(measureSpec);
        switch (mode){
            case MeasureSpec.EXACTLY:
                tempSize=size;
                break;
            case MeasureSpec.AT_MOST:
                //取可以取的最大值，也可以取任意不大于的值
                tempSize=defaultValue;
                break;
            case MeasureSpec.UNSPECIFIED:
                tempSize=defaultValue;
                break;
        }
        return tempSize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mScreenWidth=getSize(300,widthMeasureSpec);
        mScreenHeight=getSize(300,heightMeasureSpec);
        changeWidth=mScreenWidth;
        changeHeight=0;


        setMeasuredDimension(mScreenWidth,mScreenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (drawable!=null){
            canvas.clipRect(0,changeHeight,mScreenWidth,mScreenHeight, Region.Op.REPLACE);
            canvas.drawBitmap(bitmap_cpu_cover,20,0,paint);

            //radialGradient=new RadialGradient(mScreenWidth/2,changeHeight,mScreenWidth,0xffffffff,0x01ffffff, Shader.TileMode.CLAMP);
            canvas.clipRect(0,0,mScreenWidth,changeHeight, Region.Op.REPLACE);

            if (mScreenHeight-changeHeight>40){

                    //cleanPaint.setShader(radialGradient);
                    //canvas.drawArc(0,changeHeight-60,mScreenWidth,changeHeight+20,10,10,true,cleanPaint);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    canvas.drawOval(0,changeHeight-40,mScreenWidth-20,changeHeight,cleanPaint);
//                }

            }

            canvas.drawBitmap(bitmap_cpu_line,20,0,paint);

        }
        //radialGradient=null;





    }
}
