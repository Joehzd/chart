package com.ivan.chardemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.LinearInterpolator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ivan on 2017/5/6.
 */

public class MyLineChart extends SurfaceView implements SurfaceHolder.Callback {
    private int currentX;
    private int oldX;

    private SurfaceHolder surfaceHolder;

    private boolean isRunning = true;

    //表格画笔
    Paint chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    
    private LinkedList<PointF> linkedList = new LinkedList<>();
    int width, height;
    Paint mPointPaint;
    Paint mLinePaint;
    //遮罩画笔
    Paint bgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    ValueAnimator valueAnimator;

    private static float[] temp = {-9, -10.2f, 1, 1, 1, 3, 8, 10, 11, 12, 15,
            14, 18, 12, 15, 17, 13, 15, 12, 14, 11, 12, 14, 17};// 24个温度值

    private int tick = 10; // 时间间隔(ms)
    private int bottom = 150; // 坐标系地段距离框架顶端的距离
    private int top = 10; // 坐标系顶端距离框架顶端框的距离
    private int left = 30; // 坐标系左边距离框架左边框的距离
    static int right; // 坐标系右边距离框架左边的距离(!)
    static int gapX; // 两根竖线间的间隙(!)
    private int gapY = 120; // 两根横线间的间隙

    public MyLineChart(Context context) {
        super(context);
    }

    // 在这里初始化才是最初始化的。
    public MyLineChart(Context context, AttributeSet atr) {
        super(context, atr);

        setZOrderOnTop(true);// 设置置顶（不然实现不了透明）
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);// 设置背景透明
    }

    /**
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("系统消息", "surfaceCreated");

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        // 加入下面这三句是当抽屉隐藏后，打开时防止已经绘过图的区域闪烁，所以干脆就从新开始绘制。
        isRunning = true;
        currentX = 0;
        //clearCanvas();
        bgPaint = new Paint(1);
        bgPaint.setDither(true);
        bgPaint.setFilterBitmap(true);
        bgPaint.setStyle(Paint.Style.FILL);
        //drawChartLines();
        chartPaint.setColor(Color.RED);
        chartPaint.setStyle(Paint.Style.FILL);
        chartPaint.setStrokeWidth(5);
        
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.BLACK);
        mPointPaint.setStrokeWidth(12);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 用来画折线
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(6);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);


        linkedList.add(new PointF((float) (width / 5), 200f));
        linkedList.add(new PointF((float) (width / 5 * 2), 149f));
        linkedList.add(new PointF((float) (width / 5 * 3), 349f));
        linkedList.add(new PointF((float) (width / 5 * 4), 249f));



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                drawChartLines();
                drawChartLine();
                //setValueAnimators();
            }
        });

        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.i("系统信息", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("系统信息", "surfaceDestroyed");

        // 加入这个变量是为了控制抽屉隐藏时不会出现异常。
        isRunning = false;
    }

    private void drawBg(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, height);
        path.lineTo(0, linkedList.get(0).y);
        for (int i = 1; i < linkedList.size(); i++) {
            PointF point = linkedList.get(i);
            path.lineTo(width / 5 * i, point.y );
        }
        path.lineTo(width/5*4, height);
        path.close();
        // path.lineTo(0, centerY);

        Shader bgShader1 = new LinearGradient(0.0F, 0.0F, 0.0F, height, 0x992dcaff, 0x4a76ff, Shader.TileMode.CLAMP);
        bgPaint.setShader(bgShader1);
        canvas.drawPath(path, bgPaint);
    }
    private void drawChartLines() {

        Canvas canvas = surfaceHolder.lockCanvas();
        DashPathEffect effects = new DashPathEffect(new float[]{5, 10}, 1);
        chartPaint.setPathEffect(effects);
        //五组虚线
        Path path = new Path();
        path.moveTo(0, height);
        path.lineTo(width, height);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, height / 6 * 5);
        path.lineTo(width, height / 6 * 5);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, height / 6 * 4);
        path.lineTo(width, height / 6 * 4);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, height / 6 * 3);
        path.lineTo(width, height / 6 * 3);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, height / 6 * 2);
        path.lineTo(width, height / 6 * 2);
        canvas.drawPath(path, chartPaint);

        path.moveTo(0, height / 6);
        path.lineTo(width, height / 6);
        canvas.drawPath(path, chartPaint);

        //温度
        canvas.drawText(35 + "˚C", width-120, height - height / 21, chartPaint);
        canvas.drawText(37 + "˚C", width-120, height / 6 - height / 21, chartPaint);


        //四组竖线
        canvas.drawLine(width / 5, height, width / 5, height / 6, chartPaint);
        canvas.drawLine(width / 5 * 2, height, width / 5 * 2, height / 6, chartPaint);
        canvas.drawLine(width / 5 * 3, height, width / 5 * 3, height / 6, chartPaint);
        canvas.drawLine(width / 5 * 4, height, width / 5 * 4, height / 6, chartPaint);
        canvas.drawLine(width / 5 * 5, height, width / 5 * 5, height / 6, chartPaint);
        canvas.drawLine(width / 5 * 6, height, width / 5 * 6, height / 6, chartPaint);

        surfaceHolder.unlockCanvasAndPost(canvas);

    }


    private  void setValueAnimator2(){
        Canvas canvas;



        for (int i=0;i<width/5;i+=16){
            clearCanvas();
            canvas=surfaceHolder.lockCanvas();
           canvas.translate(-i,0);
//
//            LinkedList<PointF> tempList=copyData(linkedList);
//            for (int k=0;k<tempList.size();k++){
//                linkedList.get(k).set(tempList.get(k).x-i,tempList.get(k).y);
//
//            }
            Path path =new Path();

            for (int j = 0; j < linkedList.size() - 1; j++) {

                canvas.drawCircle(width / 5 * (j + 1), linkedList.get(j + 1).y, 10, mPointPaint);
                canvas.drawLine(width / 5 * j, linkedList.get(j).y, width / 5 * (j + 1), linkedList.get(j + 1).y, mLinePaint);

                path.moveTo(0, height);
                path.lineTo(0, linkedList.get(0).y);
                for (int ii = 1; ii < linkedList.size(); ii++) {
                    PointF point = linkedList.get(ii);
                    path.lineTo(width / 5 * ii, point.y );
                }
                path.lineTo(width/5*4, height);
                path.close();
                // path.lineTo(0, centerY);

                Shader bgShader1 = new LinearGradient(0.0F, 0.0F, 0.0F, height, 0x992dcaff, 0x4a76ff, Shader.TileMode.CLAMP);
                bgPaint.setShader(bgShader1);
                canvas.drawPath(path, bgPaint);
            }
            canvas.translate(i,0);

//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }
    private void setValueAnimators(){
        valueAnimator= ValueAnimator.ofFloat(0,width/5);

        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp= (float) animation.getAnimatedValue();
                Canvas canvas=surfaceHolder.lockCanvas();

                LinkedList<PointF> tempList=copyData(linkedList);
                for (int i=0;i<tempList.size();i++){
                    linkedList.get(i).set(tempList.get(i).x-temp,tempList.get(i).y);

                }
                for (int j = 0; j < linkedList.size() - 1; j++) {

                    canvas.drawCircle(width / 5 * (j + 1), linkedList.get(j + 1).y, 10, mPointPaint);
                    canvas.drawLine(width / 5 * j, linkedList.get(j).y, width / 5 * (j + 1), linkedList.get(j + 1).y, mLinePaint);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        });

    }

    protected void GridDraw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        float max = temp[0];
        float temMax = 0;
        float min = temp[0];
        float temMin = 0;
        float space = 0f;// 平均值
        for (int i = 1; i < temp.length; i++) {
            if (max < temp[i]) {
                max = temp[i];
            }
            if (min > temp[i]) {
                min = temp[i];
            }
            temMax = max;
            temMin = min;
        }
        space = (temMax - temMin) / 7;// 7段有效显示范围
        // textY=Math.round(temMin + space * i);

        Paint mbackLinePaint = new Paint();// 用来画坐标系了
        mbackLinePaint.setColor(Color.WHITE);
        mbackLinePaint.setAntiAlias(true);
        mbackLinePaint.setStrokeWidth(1);
        mbackLinePaint.setStyle(Paint.Style.FILL);

        Paint mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        // mTextPaint.setTextAlign(Align.RIGHT);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(12F);// 设置温度值的字体大小
        // 绘制坐标系
        for (int i = 0; i < 8; i++) {
            canvas.drawLine(left, top + gapY * i, left + gapX * 23, top + gapY
                    * i, mbackLinePaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            float result = temMin + space * i;// 精确的各个节点的值
            BigDecimal b = new BigDecimal(result);// 新建一个BigDecimal
            float displayVar = b.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .floatValue();// 进行小数点一位保留处理现实在坐标系上的数值
            canvas.drawText("" + displayVar, left - 2, bottom + 3 - 20 * i,
                    mTextPaint);
        }
        for (int i = 0; i < 24; i++) {
            canvas.drawLine(left + gapX * i, top, left + gapX * i, bottom,
                    mbackLinePaint);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            //canvas.drawText(houres[i], left + gapX * i, bottom + 14, mTextPaint);
        }
    }

    private void drawChartLine() {

        clearCanvas();
        while (isRunning) {
            try {
                drawChart(currentX);// 绘制

                currentX += 9;// 往前进

                if (currentX > right) {
                    // 如果到了终点，则清屏重来
                    // todo 平移所画的路径
                    //clearCanvas();
                    isRunning=false;
                    setValueAnimator2();
                    //valueAnimator.start();


                }


            } catch (Exception e) {

            }
        }
    }

    void drawChart(int length) {

        if (length == 0)
            oldX = 0;
        Canvas canvas = surfaceHolder.lockCanvas(new Rect(oldX, 0, oldX + length, height));// 范围选取正确
        //Log.i("系统消息-drawChart", "oldX = " + oldX + "  length = " + length);
        Path path = new Path();
        for (int j = 0; j < linkedList.size() - 1; j++) {

            canvas.drawCircle(width / 5 * (j + 1), linkedList.get(j + 1).y, 10, mPointPaint);
            canvas.drawLine(width / 5 * j, linkedList.get(j).y, width / 5 * (j + 1), linkedList.get(j + 1).y, mLinePaint);


            path.moveTo(0, height);
            path.lineTo(0, linkedList.get(0).y);
            for (int i = 1; i < linkedList.size(); i++) {
                PointF point = linkedList.get(i);
                path.lineTo(width / 5 * i, point.y );
            }
            path.lineTo(width/5*4, height);
            path.close();
            // path.lineTo(0, centerY);

            Shader bgShader1 = new LinearGradient(0.0F, 0.0F, 0.0F, height/2, 0x992dcaff, 0x4a76ff, Shader.TileMode.CLAMP);
            bgPaint.setShader(bgShader1);
            canvas.drawPath(path, bgPaint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
    }



    //深复制
    private LinkedList<PointF> copyData(List<PointF> points) {
        LinkedList<PointF> data = new LinkedList<>();
        for (int i = 0; i < points.size(); i++) {
            PointF point = points.get(i);
            data.add(new PointF(point.x, point.y));
        }
        return data;
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