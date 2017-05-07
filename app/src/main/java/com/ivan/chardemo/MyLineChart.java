package com.ivan.chardemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.math.BigDecimal;

/**
 * Created by ivan on 2017/5/6.
 */

public class MyLineChart extends SurfaceView implements SurfaceHolder.Callback {
    private int currentX;

    private int oldX;

    private SurfaceHolder surfaceHolder;

    private boolean isRunning = true;

    private static float[] temp = { -9, -10.2f, 1, 1, 1, 3, 8, 10, 11, 12, 15,
            14, 18, 12, 15, 17, 13, 15, 12, 14, 11, 12, 14, 17 };// 24个温度值

    private String[] houres = { "00", "01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23" };// 一天的时间24H

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

        // 加入下面这三句是当抽屉隐藏后，打开时防止已经绘过图的区域闪烁，所以干脆就从新开始绘制。
        isRunning = true;
        currentX = 0;
        clearCanvas();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                //gridDraw();
                drawChartLine();
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

    protected void gridDraw() {
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
        space = (temMax - temMin) / 7;

        Canvas canvas = surfaceHolder.lockCanvas();

        Paint mbackLinePaint = new Paint();// 用来画坐标系了
        mbackLinePaint.setColor(Color.WHITE);
        mbackLinePaint.setAntiAlias(true);
        mbackLinePaint.setStrokeWidth(3);
        mbackLinePaint.setStyle(Paint.Style.FILL);

        Paint mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
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
            canvas.drawText(houres[i], left + gapX * i, bottom + 14, mTextPaint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
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
            canvas.drawText(houres[i], left + gapX * i, bottom + 14, mTextPaint);
        }
    }

    private void drawChartLine() {
        while (isRunning) {
            try {
                drawChart(currentX);// 绘制

                currentX++;// 往前进

                if (currentX == right) {
                    // 如果到了终点，则清屏重来
                    clearCanvas();
                    currentX = 0;
                }

//                try {
//                    Thread.sleep(tick);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
            } catch (Exception e) {

            }
        }
    }

    void drawChart(int length) {
        if (length == 0)
            oldX = 0;
        Canvas canvas = surfaceHolder.lockCanvas(new Rect(oldX, 0, oldX + length, 180));// 范围选取正确
         Log.i("系统消息", "oldX = " + oldX + "  length = " + length);
        Paint mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.BLACK);

        Paint mLinePaint = new Paint();// 用来画折线
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setStyle(Paint.Style.FILL);

        float max = temp[0];
        float temMax = 0;
        float min = temp[0];
        float temMin = 0;
        float spacePX = 0f;// 平均像素值
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
        spacePX = 240 / (temMax - temMin);// 平均每个温度值说占用的像素值

        float cx = 0f;
        float cy = 0f;
        float dx = 0f;
        float dy = 0f;
        for (int j = 0; j < temp.length - 1; j++) {
            cx = left + gapX * j;
            cy = bottom - (temp[j] - temMin) * spacePX;
            dx = left + gapX * (j + 1);
            dy = bottom - (temp[j + 1] - temMin) * spacePX;
            canvas.drawCircle(cx, cy, 3, mPointPaint);
            canvas.drawLine(cx, cy, dx, dy, mLinePaint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
    }

    /**
     * 把画布擦干净，准备绘图使用。
     */
    private void clearCanvas() {
        Canvas canvas = surfaceHolder.lockCanvas();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清除画布

        GridDraw(canvas);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}