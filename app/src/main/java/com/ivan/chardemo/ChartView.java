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

    //三种点画笔
    Paint bluePointPaint;

    Paint redPointPaint;

    Paint pointPaint;

    //线条画笔
    Paint linePaint;

    //遮罩画笔
    Paint bgPaint;

    //温度画笔
    private Paint temperaturePaint;

    private float pointSize = 26f;
    private float redPointSize = 18f;
    private float newDataSize = 5f;
    private int TEMPERATURE_MAX = 40;
    private int TEMPERATURE_MIN = 30;

    private int TEMPERATURE_MAX_C = 40;
    private int TEMPERATURE_MIN_C = 30;
    private Path path = new Path();
    private ArrayList<PointF> tempList;

    // 四种移动动画
    private ValueAnimator valueAnimator;
    private ValueAnimator verticalAnimator;
    private ValueAnimator newDataAnimator;
    private ValueAnimator firstAnimator;

    private float mProgress;    //  动画进度

    private int centerX, centerY, mWidth, mHeight;
    private boolean isRuning = false;
    private boolean isNewData = false;
    private boolean isFirst = true;
    private boolean isOnce = true;

    private float firstValue = 0.0f;
    private LinkedList<PointF> newDataList = new LinkedList<>();

    private List<PointF> linkedList = Collections.synchronizedList(new LinkedList<PointF>());

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1234:
                    isRuning = false;
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
        initData();

    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initData();


    }

    /**
     * 初始化数据
     */
    private void initData() {
        mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        if (linkedList==null){
            linkedList=new LinkedList<>();
        }
        linkedList.clear();
        linkedList.add(new PointF(0f, 200f));
        linkedList.add(new PointF(mWidth / 5, 149f));
        linkedList.add(new PointF(mWidth / 5 * 2, 349f));
        linkedList.add(new PointF(mWidth / 5 * 3, 249f));
        linkedList.add(new PointF(mWidth / 5 * 4, 269f));

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
        bluePointPaint.setColor(Color.parseColor("#2A99FF"));
        bluePointPaint.setStyle(Paint.Style.FILL);
        bluePointPaint.setStrokeWidth(16);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(5);

        temperaturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        temperaturePaint.setColor(Color.WHITE);
        temperaturePaint.setStyle(Paint.Style.FILL);
        //Typeface typeface=Typeface.create("big", Typeface.BOLD);
        //temperaturePaint.setTypeface(typeface);
        temperaturePaint.setStrokeWidth(6);
        temperaturePaint.setTextSize(50);
        tempList = copyData(linkedList);

        animatorFirst();
        animatorHorizontial();
        animatorHtoNewData();
        animatorVertical();
    }


    private void animatorFirst() {
        final float[] saveTemp = {0f};
        firstAnimator = ValueAnimator.ofFloat(0.1f, 1.0f);
        firstAnimator.setInterpolator(new LinearInterpolator());

        firstAnimator.setEvaluator(new FloatEvaluator());
        firstAnimator.setDuration(2000);
        firstAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();
                float temp = value * (mWidth / 5 * 4 + 27);
                System.out.println(saveTemp[0]);
                firstValue = firstValue + (temp - saveTemp[0]);
                saveTemp[0] = temp;
                if (value != 0) {
                    System.out.println("firstValue=" + firstValue);
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
                firstValue = 0;
                isRuning = true;
                isFirst = false;
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
    private void animatorHtoNewData() {

        if (linkedList.size() < 0) {
            return;
        }

        newDataAnimator = ValueAnimator.ofFloat(0, 1);
        newDataAnimator.setInterpolator(new LinearInterpolator());
        newDataAnimator.setDuration(600);
        newDataAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                newDataSize = newDataSize + 5;
                if (value != 0) {
                    //System.out.println("value="+value);
                }
                isNewData = true;
                invalidate();
            }
        });
        newDataAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                isNewData = false;
                newDataSize = 5f;
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

        isNewData = false;
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();
                float temp = value * (mWidth / 5);
                if (value != 0) {
                    //System.out.println("value="+value);
                }
                pointSize = (float) (pointSize - 0.2);
                redPointSize = (float) (redPointSize - 0.2);
                for (int i = 0; i < tempList.size(); i++) {

                    linkedList.get(i).set((tempList.get(i).x) - temp, tempList.get(i).y);


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
                if (linkedList != null && linkedList.size() < 5) {
                    if (newDataList.size() < 0) {
                        linkedList.add(new PointF((float) (mWidth / 5 * 4), (float) Math.random() * 300 + 100));
                    } else {
                        linkedList.add(newDataList.get(0));
                    }

                }
                tempList = copyData(linkedList);
                isNewData = false;
                pointSize = 26f;
                redPointSize = 18f;
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


        verticalAnimator = ValueAnimator.ofFloat(0, 1);
        verticalAnimator.setInterpolator(new LinearInterpolator());
        verticalAnimator.setDuration(700);
        verticalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                TEMPERATURE_MIN = TEMPERATURE_MIN_C;
                TEMPERATURE_MAX = TEMPERATURE_MAX_C;
                float temp = 0;
                if (temperatureCount > 0) {
                    //此处本应为mHeight/6*3/10*temperatureCount，简写如下
                    temp = value * (mHeight / 20 * temperatureCount);
                } else {
                    temp = value * (mHeight / 20 * Math.abs(temperatureCount));
                }


                if (value != 0) {
                    //System.out.println("vertical="+temp);
                }
                for (int i = 0; i < tempList.size(); i++) {

                    if (temperatureCount < 0) {
                        linkedList.get(i).set((tempList.get(i).x), (tempList.get(i).y) - temp);
                    } else if (temperatureCount > 0) {
                        linkedList.get(i).set((tempList.get(i).x), (tempList.get(i).y) + temp);
                    } else {
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

                isRuning = true;
                temperatureCount = 0;
                tempList = copyData(linkedList);
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


    //复制list
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
     * @param duration 动画持续时间
     */
    public void startAnim(long duration) {
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
    // TODO: 2017/5/12 先不用
    public void setPercentage(float percentage) {
        if (percentage < 0.0f || percentage > 1.0f) {
            throw new IllegalArgumentException(
                    "setPercentage not between 0.0f and 1.0f");
        }
        mProgress = percentage;
        invalidate();
    }

    // TODO: 2017/5/12 先不用
    private void setAnim(Canvas canvas) {

        PathMeasure measure = new PathMeasure(path, false);
        float pathLength = measure.getLength();
        PathEffect effect = new DashPathEffect(new float[]{0,
                pathLength}, pathLength - pathLength * mProgress);
        linePaint.setPathEffect(effect);
        canvas.drawPath(path, linePaint);
    }


    @Override
    protected void onDraw(final Canvas canvas) {

        drawChartLine(canvas);
        drawPointLine(canvas);
        drawBg(canvas);
        drawPoint(canvas);


        if (isFirst) {
            canvas.save();
            canvas.clipRect(firstValue, 0, mWidth / 5 * 4 + 27, mHeight, Region.Op.REPLACE);
            canvas.drawColor(Color.parseColor("#087DFF"));
            canvas.restore();
        }
        if (isNewData) {
            canvas.clipRect(mWidth / 5 * 3 + newDataSize, 0, mWidth / 5 * 4 + 27, mHeight, Region.Op.REPLACE);
            canvas.drawColor(Color.parseColor("#087DFF"));
        }
        if (isOnce) {
            handler.removeMessages(0x1235);
            handler.sendEmptyMessage(0x1235);
            isOnce = false;
        }
        if (isRuning) {
            handler.removeMessages(0x1234);
            handler.sendEmptyMessage(0x1234);
        }


    }

    /**
     * 画折线的下部覆盖涂层
     */
    private void drawBg(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, centerY);
        path.lineTo(linkedList.get(0).x, linkedList.get(0).y);
        int k = 0;
        for (int i = 1; i < linkedList.size(); i++) {
            PointF point = linkedList.get(i);
            path.lineTo(point.x, point.y);
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

        for (int i = 1; i < linkedList.size() - 1; i++) {

            canvas.drawCircle(linkedList.get(i).x, linkedList.get(i).y, 16f, pointPaint);
        }
        canvas.drawCircle(linkedList.get(linkedList.size() - 1).x, linkedList.get(linkedList.size() - 1).y, pointSize, pointPaint);

        if (linkedList.get(linkedList.size() - 1).y <= linkedList.get(linkedList.size() - 2).y) {
            canvas.drawCircle(linkedList.get(linkedList.size() - 1).x, linkedList.get(linkedList.size() - 1).y, redPointSize, redPointPaint);

        } else {
            canvas.drawCircle(linkedList.get(linkedList.size() - 1).x, linkedList.get(linkedList.size() - 1).y, redPointSize, bluePointPaint);

        }


    }

    //画线
    private void drawPointLine(Canvas canvas) {

        for (int i = 0; i < linkedList.size() - 1; i++) {

            canvas.drawLine(linkedList.get(i).x, linkedList.get(i).y, linkedList.get(i + 1).x, linkedList.get(i + 1).y, linePaint);

        }

    }


    /**
     * 创建直线点集
     *
     * @return
     */
    //TODO: 2017/5/12 先不用
    private final int REFRESH = 20;

    private LinkedList<PointF> buildDetailPoints() {
        LinkedList<PointF> pointss = new LinkedList<>();
        for (int i = 0; i < linkedList.size(); i++) {
            ArrayList<Float> x = divideXY(linkedList.get(i + 1).x, linkedList.get(i).x);
            ArrayList<Float> y = divideXY(linkedList.get(i + 1).y, linkedList.get(i).y);
            for (int t = 0; t < x.size(); t++) {
                // 直线切分点集
                pointss.add(new PointF(x.get(t), y.get(t)));
            }
        }

        return pointss;
    }

    /**
     * 直线切分
     *
     * @param xy1 点1
     * @param xy2 点2
     * @return
     */
    private ArrayList<Float> divideXY(float xy2, float xy1) {
        ArrayList<Float> arrayList = new ArrayList<>();
        for (int i = 0; i < REFRESH; i++) {
            arrayList.add((xy2 - xy1) / REFRESH * i);
        }
        return arrayList;
    }


    /**
     * 温度-像素 转换
     *
     * @param temperature 温度
     * @return 屏幕的像素高度
     */
    private float temperatureToPix(float temperature) {
        float pix = 0f;
        //每一度所代表的像素高度
        //System.out.println(temperature);
        float single = (centerY - centerY / 5 * 2) / (TEMPERATURE_MAX_C - TEMPERATURE_MIN_C);
        pix = centerY - centerY / 5 - (temperature - TEMPERATURE_MIN_C) * single;

        return pix;

    }


    /**
     *
     * view结束时回收各种资源
     * */

    public void recycleResourse(){
        if (firstAnimator!=null){
            firstAnimator.cancel();
        }
        if (valueAnimator!=null){
            valueAnimator.cancel();
        }
        if (newDataAnimator!=null){
            newDataAnimator.cancel();
        }
        if (verticalAnimator!=null){
            verticalAnimator.cancel();
        }
        isRuning=false;
        isFirst=false;
        isOnce=false;
        isNewData=false;
    }
    /**
     * 设置温度
     */

    //温度差计数器
    private int temperatureCount = 0;

    public void setTempurature(float temperature,boolean is) {
        temperatureCount=0;
        while (temperature > TEMPERATURE_MAX_C) {
            TEMPERATURE_MAX_C++;
            TEMPERATURE_MIN_C++;
            temperatureCount++;
        }
        while (temperature <= TEMPERATURE_MIN_C) {
            TEMPERATURE_MAX_C--;
            TEMPERATURE_MIN_C--;
            temperatureCount--;
        }
        if (newDataList==null){
            newDataList=new LinkedList<>();
        }
        newDataList.clear();
        newDataList.add(new PointF(mWidth / 5 * 4, temperatureToPix(temperature)));
    }

    //绘制温度刻度
    private void drawChartLine(Canvas canvas) {

        //温度
        temperaturePaint.setStyle(Paint.Style.FILL);
        temperaturePaint.setTextSize(50);
        canvas.drawText(TEMPERATURE_MIN + "", mWidth - 120, centerY - centerY / 4, temperaturePaint);

        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.STROKE);
        temperaturePaint.setStrokeWidth(4);
        canvas.drawCircle(mWidth - 50, centerY - centerY / 3 + 20, 9f, temperaturePaint);


        temperaturePaint.setStrokeWidth(6);
        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.FILL);
        canvas.drawText("c", mWidth - 40, centerY - centerY / 4, temperaturePaint);

        ///////
        temperaturePaint.setStyle(Paint.Style.FILL);
        temperaturePaint.setTextSize(50);
        canvas.drawText(TEMPERATURE_MAX + "", mWidth - 120, centerY / 6, temperaturePaint);

        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.STROKE);
        temperaturePaint.setStrokeWidth(4);
        canvas.drawCircle(mWidth - 50, centerY / 7 - 15, 9f, temperaturePaint);
        temperaturePaint.setStrokeWidth(6);


        temperaturePaint.setTextSize(40);
        temperaturePaint.setStyle(Paint.Style.FILL);
        canvas.drawText("c", mWidth - 40, centerY / 6, temperaturePaint);
        temperaturePaint.setStyle(Paint.Style.FILL);

    }
}
