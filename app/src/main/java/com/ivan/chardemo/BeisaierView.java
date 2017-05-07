package com.ivan.chardemo;

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

    public BeisaierView(Context context) {
        super(context);
    }

    public BeisaierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        path=new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0,100);
        path.quadTo(100f,129f,100f,129f);
        path.quadTo(150f,169f,150f,169f);
        canvas.drawPath(path,paint);
    }
}
