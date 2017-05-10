package com.ivan.chardemo;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ivan on 2017/5/6.
 */

public class ChartView extends View {


    //表格画笔
    Paint chartPaint;
    //点画笔
    Paint pointPaint ;
    //线条画笔
    Paint linePaint ;

    //遮罩画笔
    Paint bgPaint;

    //温度画笔
    Paint temPaint;

    Path path = new Path();
    ArrayList<PointF> tempList;
    ValueAnimator valueAnimator;
    ValueAnimator newDataAnimator;
    private float mProgress;    //  动画进度

    int centerX, centerY, mWidth, mHeight;
    boolean isRuning;
    boolean isNewData=false;
    LinkedList<PointF> newDataList=new LinkedList<>();

    List<PointF> linkedList = Collections.synchronizedList(new LinkedList<PointF>());

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234) {


                //linkedList.add(new PointF((float) (mWidth / 5*4), (float)Math.random()*300+100));

                isRuning=false;
                valueAnimator.start();

                //startAnim(1000);

            }
        }
    };
    public ChartView(Context context) {
        super(context);

    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        isRuning=true;
        mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        linkedList.add(new PointF( 0f, 200f));
        linkedList.add(new PointF(mWidth / 5, 149f));
        linkedList.add(new PointF(mWidth / 5 * 2, 349f));
        linkedList.add(new PointF(mWidth / 5 * 3, 249f));
        linkedList.add(new PointF(mWidth / 5 * 4, 269f));

        bgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint = new Paint(1);
        bgPaint.setDither(true);
        bgPaint.setFilterBitmap(true);
        bgPaint.setStyle(Paint.Style.FILL);


        chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setColor(Color.BLACK);
        chartPaint.setStyle(Paint.Style.STROKE);
        chartPaint.setStrokeWidth(1);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(16);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLUE);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(10);

        temPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        temPaint.setColor(Color.BLACK);
        temPaint.setStyle(Paint.Style.FILL);
        Typeface typeface=Typeface.create("big", Typeface.BOLD);
        temPaint.setTypeface(typeface);
        temPaint.setStrokeWidth(6);
        temPaint.setTextSize(50);
        tempList =copyData(linkedList);

        animatorH();
        animatorHtoNewData();


    }

    private void animatorHtoNewData(){

        //
        if (linkedList.size()<0){
            return;
        }
        final float randomy=(float)Math.random()*300+100;
        final float px=mWidth/5*4-linkedList.get(linkedList.size()-2).x;
        final float py=randomy-linkedList.get(linkedList.size()-2).y;

        newDataAnimator= ValueAnimator.ofFloat(0,1);
        newDataAnimator.setInterpolator(new LinearInterpolator());
        newDataAnimator.setDuration(1000);
        newDataAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                float temp=value*(mWidth/5);
                if (value!=0){
                    System.out.println("value="+value);
                }

                newDataList.clear();
                newDataList.add(new PointF(mWidth/5*(4+value),randomy+py*value));
                isNewData=true;
                invalidate();
            }
        });
        newDataAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


                isRuning=true;
                isNewData=false;
                //linkedList.add(new PointF((float) (mWidth / 5*4), (float)Math.random()*300+100));

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
    private void animatorH() {

        isNewData=false;
        valueAnimator= ValueAnimator.ofFloat(0,1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                float temp=value*(mWidth/5);
                if (value!=0){
                    System.out.println("value="+value);
                }
                for (int i=0;i<tempList.size();i++){

                    linkedList.get(i).set((tempList.get(i).x)-temp,tempList.get(i).y);
                    //System.out.println(tempList.get(i).x+" -- "+ linkedList.get(i).x);

                }
                invalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


                if (linkedList != null) {
                    while (linkedList.size() > 0 && linkedList.get(0).x < 0) {
                        linkedList.remove(0);

                    }
                }
                if (linkedList.size()<5)
                {
                    linkedList.add(new PointF((float) (mWidth / 5*4), (float)Math.random()*300+100));
                }


                tempList =copyData(linkedList);
                //invalidate();
                isNewData=true;

                newDataAnimator.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        centerX = 0;
        centerY = h;

    }


    //深复制
    private ArrayList<PointF> copyData(List<PointF> points) {
        //System.out.println(points.get(0).x);
        ArrayList<PointF> data = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            PointF point = points.get(i);
            data.add(new PointF(point.x, point.y));
        }
        return data;
    }

    /**
     *
     * @param duration      动画持续时间
     */
    public void startAnim( long duration) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "percentage", 0.0f, 1.0f);
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });

    }

    //  计算动画进度
    public void setPercentage(float percentage) {
        if (percentage < 0.0f || percentage > 1.0f) {
            throw new IllegalArgumentException(
                    "setPercentage not between 0.0f and 1.0f");
        }
        mProgress = percentage;
        invalidate();
    }

    private void setAnim(Canvas canvas) {

        PathMeasure measure = new PathMeasure(path, false);
        float pathLength = measure.getLength();
        PathEffect effect = new DashPathEffect(new float[]{0,
                pathLength}, pathLength - pathLength * mProgress);
        linePaint.setPathEffect(effect);
        canvas.drawPath(path, linePaint);
    }

    private void drawNewData(Canvas canvas){
        float x=linkedList.get(linkedList.size()-2).x;
        float y=linkedList.get(linkedList.size()-2).y;
        canvas.drawLine(x,y
                ,newDataList.get(0).x,newDataList.get(0).y,linePaint);
        Path newDataPath=new Path();
        newDataPath.moveTo(x,y);
        newDataPath.lineTo(newDataList.get(0).x,newDataList.get(0).y);
        newDataPath.lineTo(newDataList.get(0).x,mHeight);
        newDataPath.lineTo(x,mHeight);
        newDataPath.close();
        canvas.drawPath(newDataPath,bgPaint);
    }
    @Override
    protected void onDraw(final Canvas canvas) {

        drawChartLine(canvas);
        drawPointLine(canvas);
        drawBg(canvas);
        drawPoint(canvas);
        if (isNewData){
            canvas.clipRect(mWidth/5*3,0,mWidth/5*4,mHeight);
            drawNewData(canvas);
        }
        if (isRuning){
            handler.removeMessages(0x1234);
            handler.sendEmptyMessage(0x1234);
        }
        //setAnim(canvas);




    }

    private void drawBg(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, centerY);
        path.lineTo(0, linkedList.get(0).y);
        int k=0;
        for (int i = 1; i < linkedList.size(); i++) {
            PointF point = linkedList.get(i);
            path.lineTo(point.x, point.y );
            k++;
        }
        path.lineTo(linkedList.get(k).x, centerY);
        path.close();


        Shader bgShader1 = new LinearGradient(0.0F, 0.0F, 0.0F, centerY, 0x992dcaff, 0x4a76ff, Shader.TileMode.CLAMP);
        bgPaint.setShader(bgShader1);
        canvas.drawPath(path, bgPaint);
    }


    //画点
    private void drawPoint(Canvas canvas) {


        for (int i=1;i<linkedList.size();i++){
            canvas.drawCircle(linkedList.get(i).x, linkedList.get(i).y, 16f,pointPaint);
            //path.addCircle(mWidth / 5 * i, linkedList.get(i).y, 16f, Path.Direction.CW);
        }
       // canvas.drawPath(path, pointPaint);

    }
    //画线
    private void drawPointLine(Canvas canvas) {


        for (int i=0;i<linkedList.size()-1;i++){

            canvas.drawLine(linkedList.get(i).x, linkedList.get(i).y,linkedList.get(i+1).x, linkedList.get(i+1).y,linePaint);

        }






    }



    /**
     * 创建直线点集
     *
     * @return
     */

    private final  int REFRESH=20;
    private LinkedList<PointF> buildDetailPoints() {
        LinkedList<PointF> pointss = new LinkedList<>();
        for (int i=0;i<linkedList.size();i++){
            ArrayList<Float> x=divideXY(linkedList.get(i+1).x,linkedList.get(i).x);
            ArrayList<Float> y=divideXY(linkedList.get(i+1).y,linkedList.get(i).y);
            for (int t = 0; t<x.size(); t++) {
                // 直线切分点集
                pointss.add(new PointF(x.get(t),y.get(t)));
            }
        }

        return pointss;
    }

    /**
     * 直线切分算法

     * @param xy1 点1
     * @param xy2 点2
     * @return
     */
    private ArrayList<Float> divideXY(float xy2,  float xy1) {
        ArrayList<Float> arrayList=new ArrayList<>();
        for (int i=0;i<REFRESH;i++){
            arrayList.add((xy2-xy1)/REFRESH*i);
        }
        return arrayList;
    }



    //绘制基准线
    private void drawChartLine(Canvas canvas) {

        DashPathEffect effects = new DashPathEffect(new float[]{5, 10}, 1);
        chartPaint.setPathEffect(effects);
        //五组虚线
        Path path = new Path();
        path.moveTo(0, centerY);
        path.lineTo(mWidth, centerY);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, centerY / 6 * 5);
        path.lineTo(mWidth, centerY / 6 * 5);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, centerY / 6 * 4);
        path.lineTo(mWidth, centerY / 6 * 4);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, centerY / 6 * 3);
        path.lineTo(mWidth, centerY / 6 * 3);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, centerY / 6 * 2);
        path.lineTo(mWidth, centerY / 6 * 2);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, centerY / 6);
        path.lineTo(mWidth, centerY / 6);
        canvas.drawPath(path, chartPaint);

        //温度
        canvas.drawText(35 + "˚C", mWidth-120, centerY - centerY / 21, temPaint);
        canvas.drawText(37 + "˚C", mWidth-120, centerY / 6 - centerY / 21, temPaint);


        //四组竖线
        canvas.drawLine(mWidth / 5, centerY, mWidth / 5, centerY / 6, chartPaint);
        canvas.drawLine(mWidth / 5 * 2, centerY, mWidth / 5 * 2, centerY / 6, chartPaint);
        canvas.drawLine(mWidth / 5 * 3, centerY, mWidth / 5 * 3, centerY / 6, chartPaint);
        canvas.drawLine(mWidth / 5 * 4, centerY, mWidth / 5 * 4, centerY / 6, chartPaint);
        canvas.drawLine(mWidth / 5 * 5, centerY, mWidth / 5 * 5, centerY / 6, chartPaint);
        canvas.drawLine(mWidth / 5 * 6, centerY, mWidth / 5 * 6, centerY / 6, chartPaint);


    }
}
