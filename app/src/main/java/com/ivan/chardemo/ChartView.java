package com.ivan.chardemo;

import android.animation.Animator;
import android.animation.FloatEvaluator;
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
import android.graphics.Region;
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

    //点画笔
    Paint bluePointPaint ;

    Paint redPointPaint ;

    Paint pointPaint ;
    //线条画笔
    Paint linePaint ;

    //遮罩画笔
    Paint bgPaint;

    //温度画笔
    private Paint temperaturePaint;

    private float pointSize=26f;
    private float redPointSize=18f;
    private float newDataSize=5f;
    private float TEMPERATURE_MAX=400f;
    private float TEMPERATURE_MIN=200f;


    private Path path = new Path();
    private ArrayList<PointF> tempList;
    private ValueAnimator valueAnimator;
    private ValueAnimator verticalAnimator;
    private ValueAnimator newDataAnimator;
    private ValueAnimator firstAnimator;
    private float mProgress;    //  动画进度

    private int centerX, centerY, mWidth, mHeight;
    private boolean isRuning=false;
    private boolean isNewData=false;
    private  boolean isFirst=true;
    private  boolean isOnce=true;

    private  float firstValue=0.0f;
    private LinkedList<PointF> newDataList=new LinkedList<>();

    private List<PointF> linkedList = Collections.synchronizedList(new LinkedList<PointF>());

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1234:
                    isRuning=false;
                    valueAnimator.start();
                    break;
                case 0x1235:

                    firstAnimator.start();
                    break;

            }

        }
    };
    public ChartView(Context context) {
        super(context);

    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initData();

        final int[] count = {0};
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true){
//                        Thread.sleep(10);
//                        count[0] +=10;
//
//                        firstValue=(count[0]);
//                        invalidate();
//                        if (count[0]>=mHeight){
//                            isFirst=false;
//                            isRuning=true;
//                            return;
//                        }
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();



    }

    /**
     * 初始化数据
     * */
    private void initData() {
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


        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(16);
        redPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPointPaint.setColor(Color.parseColor("#FF2B2B"));
        redPointPaint.setStyle(Paint.Style.FILL);
        redPointPaint.setStrokeWidth(16);
        bluePointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePointPaint.setColor(Color.parseColor("#009933"));
        bluePointPaint.setStyle(Paint.Style.FILL);
        bluePointPaint.setStrokeWidth(16);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(5);

        temperaturePaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        temperaturePaint.setColor(Color.WHITE);
        temperaturePaint.setStyle(Paint.Style.FILL);
        //Typeface typeface=Typeface.create("big", Typeface.BOLD);
        //temperaturePaint.setTypeface(typeface);
        temperaturePaint.setStrokeWidth(6);
        temperaturePaint.setTextSize(50);
        tempList =copyData(linkedList);

        animatorFirst();
        animatorHorizontial();
        animatorHtoNewData();
        animatorVertical();
    }


    private void animatorFirst(){
        final float[] saveTemp = {0f};
        firstAnimator= ValueAnimator.ofFloat(0.1f,1.0f);
        firstAnimator.setInterpolator(new LinearInterpolator());

        firstAnimator.setEvaluator(new FloatEvaluator());
        firstAnimator.setDuration(2000);
        firstAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               // float aaa=animation.getAnimatedFraction();
                float value= (float) animation.getAnimatedValue();
                float temp=value*(mWidth/5*4+27);
                System.out.println(saveTemp[0]);
                firstValue=firstValue+(temp-saveTemp[0]);
                saveTemp[0] =temp;
                if (value!=0){
                    System.out.println("firstValue="+firstValue);
                }

                invalidate();
            }
        });

        firstAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                System.out.println(animation.getDuration());

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                firstValue=0;
                isRuning=true;
                isFirst=false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    /***
     * 新数据增加动画
     *
     **/
    private void animatorHtoNewData(){

        if (linkedList.size()<0){
            return;
        }
        final float randomy=(float)Math.random()*300+100;
        final float py=randomy-linkedList.get(linkedList.size()-2).y;

        newDataAnimator= ValueAnimator.ofFloat(0,1);
        newDataAnimator.setInterpolator(new LinearInterpolator());
        newDataAnimator.setDuration(600);
        newDataAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                float temp=value*(mWidth/5);
                newDataSize=newDataSize+5;
                if (value!=0){
                    //System.out.println("value="+value);
                }
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


                isNewData=false;
                newDataSize=5f;
                verticalAnimator.start();


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
    /***
     * 横向动画
     *
     **/
    private void animatorHorizontial() {

        isNewData=false;
        valueAnimator= ValueAnimator.ofFloat(0.0f,1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value= (float) animation.getAnimatedValue();
                float temp=value*(mWidth/5);
                if (value!=0){
                    //System.out.println("value="+value);
                }
                pointSize= (float) (pointSize-0.2);
                redPointSize= (float) (redPointSize-0.2);
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
                if (linkedList!=null&&linkedList.size()<5)
                {
                    linkedList.add(new PointF((float) (mWidth / 5*4), (float)Math.random()*300+100));
                }


                tempList =copyData(linkedList);
                isNewData=false;
                pointSize=26f;
                redPointSize=18f;
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

    /***
     * 竖向动画
     *
     **/
    private void animatorVertical() {

        verticalAnimator= ValueAnimator.ofFloat(0,1);
        verticalAnimator.setInterpolator(new LinearInterpolator());
        verticalAnimator.setDuration(700);
        verticalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                float temp=value*(mHeight/6);
                if (value!=0){
                    //System.out.println("vertical="+value);
                }
                for (int i=0;i<tempList.size();i++){

                    if (linkedList.get(linkedList.size()-1).y<=TEMPERATURE_MIN){
                        linkedList.get(i).set((tempList.get(i).x),(tempList.get(i).y)+temp);
                    }else if(linkedList.get(linkedList.size()-1).y>=TEMPERATURE_MAX){
                        linkedList.get(i).set((tempList.get(i).x),(tempList.get(i).y)-temp);
                    }else {
                        verticalAnimator.cancel();
                    }



                }
                invalidate();
            }
        });

        verticalAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                isRuning=true;
                tempList =copyData(linkedList);
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


        if (isFirst){
            canvas.save();
            canvas.clipRect(firstValue,0,mWidth/5*4+27,mHeight, Region.Op.REPLACE);
            canvas.drawColor(Color.parseColor("#087DFF"));
            canvas.restore();
        }
        if (isNewData){
            canvas.clipRect(mWidth/5*3+newDataSize,0,mWidth/5*4+27,mHeight, Region.Op.REPLACE);
            canvas.drawColor(Color.parseColor("#087DFF"));
        }
        if (isOnce){
            handler.removeMessages(0x1235);
            handler.sendEmptyMessage(0x1235);
            isOnce=false;
        }
        if (isRuning){
            handler.removeMessages(0x1234);
            handler.sendEmptyMessage(0x1234);
        }





    }

    private void drawBg(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, centerY);
        path.lineTo(linkedList.get(0).x, linkedList.get(0).y);
        int k=0;
        for (int i = 1; i < linkedList.size(); i++) {
            PointF point = linkedList.get(i);
            path.lineTo(point.x, point.y );
            k++;
        }
        path.lineTo(linkedList.get(k).x, centerY);
        path.close();


        Shader bgShader1 = new LinearGradient(0.0F, 0.0F, 0.0F, centerY, 0x4cffffff, 0x42ffffff, Shader.TileMode.CLAMP);
        bgPaint.setShader(bgShader1);
        canvas.drawPath(path, bgPaint);
    }


    //画点
    private void drawPoint(Canvas canvas) {

        for (int i=1;i<linkedList.size()-1;i++){

            canvas.drawCircle(linkedList.get(i).x, linkedList.get(i).y, 16f,pointPaint);
        }
        canvas.drawCircle(linkedList.get(linkedList.size()-1).x, linkedList.get(linkedList.size()-1).y, pointSize,pointPaint);

        if (linkedList.get(linkedList.size()-1).y<=linkedList.get(linkedList.size()-2).y){
            canvas.drawCircle(linkedList.get(linkedList.size()-1).x, linkedList.get(linkedList.size()-1).y, redPointSize,redPointPaint);

        }else {
            canvas.drawCircle(linkedList.get(linkedList.size()-1).x, linkedList.get(linkedList.size()-1).y, redPointSize,bluePointPaint);

        }



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



    //绘制温度
    private void drawChartLine(Canvas canvas) {

        //温度
        temperaturePaint.setStyle(Paint.Style.FILL);
        temperaturePaint.setTextSize(50);
        canvas.drawText(30+"", mWidth-120, centerY - centerY / 4, temperaturePaint);

        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.STROKE);
        temperaturePaint.setStrokeWidth(4);
        canvas.drawCircle(mWidth-50,centerY - centerY / 3+20,9f,temperaturePaint);
       // canvas.drawText("o", mWidth-120+54, centerY - centerY / 3+20, temperaturePaint);

        temperaturePaint.setStrokeWidth(6);
        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.FILL);
        canvas.drawText("c", mWidth-40, centerY - centerY / 4, temperaturePaint);

        ///////
        temperaturePaint.setStyle(Paint.Style.FILL);
        temperaturePaint.setTextSize(50);
        canvas.drawText(40 + "", mWidth-120, centerY / 6 , temperaturePaint);

        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.STROKE);
        temperaturePaint.setStrokeWidth(4);
        canvas.drawCircle(mWidth-50,centerY / 7-15,9f,temperaturePaint);
        temperaturePaint.setStrokeWidth(6);
        //canvas.drawText("o", mWidth-120+54, centerY / 7-15 , temperaturePaint);

        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.FILL);
        canvas.drawText("c", mWidth-40, centerY / 6 , temperaturePaint);
        temperaturePaint.setStyle(Paint.Style.FILL);

    }
}
