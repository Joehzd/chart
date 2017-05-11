package com.ivan.chardemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by a123 on 2017/5/11.
 */

public class ChartLineView extends View {

    int centerX, centerY, mWidth, mHeight;
    //表格画笔
    Paint vchartPaint;
    Paint hchartPaint;

    public ChartLineView(Context context) {
        super(context);
    }

    public ChartLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        hchartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hchartPaint.setColor(Color.argb(230,255,255,255));
        hchartPaint.setStyle(Paint.Style.STROKE);
        hchartPaint.setStrokeWidth(1);

        vchartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        vchartPaint.setColor(Color.argb(51,255,255,255));
        vchartPaint.setStyle(Paint.Style.STROKE);
        vchartPaint.setStrokeWidth(2);
        mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mHeight=200;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        centerX = 0;
        centerY = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChartLine(canvas);
    }

    private void drawChartLine(Canvas canvas) {

        DashPathEffect effects = new DashPathEffect(new float[]{5, 15}, 1);
        hchartPaint.setPathEffect(effects);
        //五组虚线
        Path path = new Path();
        path.moveTo(0, centerY);
        path.lineTo(mWidth, centerY);

        canvas.drawPath(path, hchartPaint);
        path.moveTo(0, centerY / 5 * 4);
        path.lineTo(mWidth, centerY / 5 * 4);
        canvas.drawPath(path, hchartPaint);

        path.moveTo(0, centerY / 5 * 3);
        path.lineTo(mWidth, centerY / 5 * 3);
        canvas.drawPath(path, hchartPaint);

        path.moveTo(0, centerY / 5 * 2);
        path.lineTo(mWidth, centerY / 5 * 2);
        canvas.drawPath(path, hchartPaint);

        path.moveTo(0, centerY / 5);
        path.lineTo(mWidth, centerY / 5);
        canvas.drawPath(path, hchartPaint);


        //四组竖线
        canvas.drawLine(mWidth / 5, centerY, mWidth / 5, centerY / 5, vchartPaint);
        canvas.drawLine(mWidth / 5 * 2, centerY, mWidth / 5 * 2, centerY / 5, vchartPaint);
        canvas.drawLine(mWidth / 5 * 3, centerY, mWidth / 5 * 3, centerY / 5, vchartPaint);
        canvas.drawLine(mWidth / 5 * 4, centerY, mWidth / 5 * 4, centerY / 5, vchartPaint);
        canvas.drawLine(mWidth / 5 * 5, centerY, mWidth / 5 * 5, centerY / 5, vchartPaint);


    }
}
