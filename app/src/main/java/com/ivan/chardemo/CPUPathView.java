package com.ivan.chardemo;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 生成贝塞尔曲线-De Casteljau算法
 */
public class CPUPathView extends View {

    public static final String TAG = "CPUPathView";
    private Paint datumPaint;
    private Paint bgPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Paint timePaint;

    private int centerX, centerY;

    private List<PointF> points;
    private float fraction;
    private ArrayList<PointF> destPoints;
    private ArrayList<PointF> finalPoints;
    private Shader bgShader1;
    private Random random;
    private int mWidth;
    private int mHeight;
    private ArrayList<PointF> mTempDestPoints;
    private LinearGradient mLineShader;
    private Bitmap mCircleBp;
    private int mCircleHeight;
    private int mCircleBpWidth;
    private boolean once = false;
    private boolean Isonce = false;
    private static int ANIM_DURATION;
    private int MAX_TEMPERATURE = 40;
    private int MIN_TEMPERATURE = 35;
    private int deviationalue = 0;
    private float offsetY = 0;

    private ValueAnimator mPathAnimator;
    private ValueAnimator mHorizontalAnimator;
    private ValueAnimator mVerticalAnimator;

    private Paint mPaint;
    private ArrayList<Float> mTemperatures;
    private float mCurrentTemperature;
    private ArrayList<Object> mTest1;

    enum Sates {STOP, START, PATH, HORIZONTAL, VERTICAL}

    private Sates mCurrentSates = Sates.START;

    private static final int RATE = 12; // 移动速率

    private int mR = 0;  // 移动速率
    private static final int HANDLER_WHAT = 100;
    private int mRate = RATE;   // 速率
    private int mState; // 状态
    private static final int STATE_READY = 0x0001;
    private static final int STATE_RUNNING = 0x0002;
    private static final int STATE_STOP = 0x0004;
    private PointF mBezierPoint = null; // 贝塞尔曲线移动点
    private Path mBezierPath = null;    // 贝塞尔曲线路径
    private Paint mBezierPaint = null;  // 贝塞尔曲线画笔
    private boolean curve = false;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mCurrentSates == Sates.PATH) {
                        mTempDestPoints = copyData(destPoints);
                        if(mHorizontalAnimator!=null){
                            mHorizontalAnimator.start();
                        }

                    }
                    break;
                case HANDLER_WHAT:
                    if (!curve) {
                        return;
                    }
                    mR +=destPoints.size();
                    if (mR >= destPoints.size()) {
                        removeMessages(HANDLER_WHAT);
                        mR = 0;
                        mState &= ~STATE_RUNNING;
                        mState &= ~STATE_STOP;
                        mState |= STATE_READY;
                        curve = false;
                        mTempDestPoints = copyData(destPoints);
                        if(mHorizontalAnimator!=null){
                            mHorizontalAnimator.start();
                        }

                        return;
                    }
                    if (mR != destPoints.size() - 1 && mR + 2 >= destPoints.size()) {
                        mR = destPoints.size() - 1;
                    }
                    // Bezier点
                    mBezierPoint = new PointF(destPoints.get(mR).x, destPoints.get(mR).y);

                    if (mR == destPoints.size() - 1) {
                        mState |= STATE_STOP;
                    }
                    invalidate();

                    break;
                default:
                    break;
            }
        }
    };



    public CPUPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density / 1.5f;
        random = new Random();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(60);

        datumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);      //基准线
        datumPaint.setColor(Color.BLACK);
        datumPaint.setStyle(Paint.Style.STROKE);
        datumPaint.setStrokeWidth(density * 1.0F);

        bgPaint = new Paint(1);
        bgPaint.setDither(true);
        bgPaint.setFilterBitmap(true);
        bgPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(1);   //线条
        linePaint.setDither(true);
        linePaint.setFilterBitmap(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(density * 6);

        CornerPathEffect cornerPathEffect = new CornerPathEffect(200);
        linePaint.setPathEffect(cornerPathEffect);

        textPaint = new Paint(1);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(density * 22);
        textPaint.setColor(Color.RED);

        timePaint = new Paint(1);
        timePaint.setTextAlign(Paint.Align.CENTER);
        timePaint.setTextSize(density * 18);
        timePaint.setColor(Color.RED);

        // 贝塞尔曲线画笔
        mBezierPaint = new Paint();
        mBezierPaint.setColor(Color.RED);
        mBezierPaint.setStrokeWidth(10);
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setAntiAlias(true);

        mCircleBp = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        mCircleHeight = mCircleBp.getHeight();
        mCircleBpWidth = mCircleBp.getWidth();

        start();

    }

    //初始化数据点和控制点的位置
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        centerX = 0;
        centerY = h;
    }

    public void initDate() {
        stop();
        curve = true;
        once = false;
        Isonce = false;

        mCurrentSates = Sates.START;
        offsetY = 0;
        if (mTemperatures != null) {
            mTemperatures.clear();
        } else {
            mTemperatures = new ArrayList<>();
        }
        mTemperatures.add(37.5f);
        if (mTempDestPoints != null) {
            mTempDestPoints.clear();

        }

        if (points != null) {
            points.clear();
        } else {
            points = new ArrayList<>();
        }

        if (destPoints != null) {
            destPoints.clear();
        } else {
            destPoints = new ArrayList<>();
        }

        MIN_TEMPERATURE = 35;
        MAX_TEMPERATURE = 40;

        if (random == null) {
            random = new Random();
        }

        if (mWidth == 0) {
            mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            mHeight = 200;
            centerY = mHeight - mHeight / 8;
        }
        points.add(new PointF(0, centerY - centerY/6));
        points.add(new PointF(mWidth / 5, centerY - centerY/6 + random.nextInt(mHeight / 5)));
        points.add(new PointF(mWidth / 5* 2, centerY - centerY / 6 * 2 + random.nextInt(mHeight / 5)));
        points.add(new PointF(mWidth / 5* 3, centerY - centerY / 6 * 3 + random.nextInt(mHeight / 5)));
        points.add(new PointF(mWidth / 5* 5, centerY - centerY / 6 * 5 + random.nextInt(mHeight / 5)));

        mCurrentTemperature = 37.5f;

        ANIM_DURATION = 1000;
        mBezierPath = new Path();
        mBezierPoint = null;

//        if (mBezierPoint == null) {
        mBezierPath.reset();
        mBezierPoint = points.get(0);
        mBezierPath.moveTo(mBezierPoint.x, mBezierPoint.y);
//            L.d(TAG, "onDraw: mBezierPath");
//        }

        //destPoints = buildBezierPoints();
        destPoints =new ArrayList<>();
        destPoints= (ArrayList<PointF>) points;
        finalPoints=buildDetailPoints();

        mState &= ~STATE_READY;
        mState |= STATE_RUNNING;
        invalidate();

        handler.removeMessages(HANDLER_WHAT);
        handler.sendEmptyMessage(HANDLER_WHAT);

        forwardAnimation();
        translationAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                setTemperature(random.nextInt(10)+20,false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private static final int[] a = {0xccffd5bb, 0x66ffe9db};
    private static final float[] b = {0.0F, 1.0F};
    private static final int[] c = {0xc7a4, -1};
    private static final float[] d = {0.0F, 1.0F};

    public void transformShader() {
        bgShader1 = new LinearGradient(0.0F, 30, 0.0F, centerY, 0x992dcaff, 0x4a76ff, Shader.TileMode.CLAMP);
        bgPaint.setShader(bgShader1);

        mLineShader = new LinearGradient(0.0F, 30, 0.0F, centerY, -13369410, -14497025, Shader.TileMode.CLAMP);
        linePaint.setShader(mLineShader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        if (curve) {
//            // Bezier曲线
//            mBezierPath.lineTo(mBezierPoint.x, mBezierPoint.y);
//            //L.d(TAG, "onDraw: mBezierPoint.x"+mBezierPoint.x+"mBezierPoint.y"+mBezierPoint.y);
//
//            canvas.drawPath(mBezierPath, linePaint);
//
//            handler.removeMessages(HANDLER_WHAT);
//            handler.sendEmptyMessage(HANDLER_WHAT);
//
//        } else {

            if(destPoints!=null){
                if (destPoints.size() > 0) {
                    //动态的
                    drawPath(canvas, destPoints);
                }
            }

       // }

        //绘制基准线
        drawDatumLine(canvas);


        if (once) {
            once = false;
            drawPoint(canvas, points, Color.BLACK);
            Message message = new Message();
            message.what = 1;
            handler.sendMessageDelayed(message, 500);
        }

    }

    // 绘制数据点
    private void drawPoint1(Canvas canvas, List<PointF> data, int color) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(20);
        for (int i = 0; i < data.size(); i++) {
            PointF pointF = data.get(i);
            canvas.drawPoint(pointF.x, pointF.y, mPaint);
        }
    }

    // 绘制点
    private void drawPoint(Canvas canvas, List<PointF> data, int color) {
        if (destPoints.size() > 0) {
            float x = destPoints.get(destPoints.size() - 1).x - mCircleBpWidth / 2;
            float y = destPoints.get(destPoints.size() - 1).y - mCircleHeight / 2;
            //L.d(TAG, "drawPoint: x" + x + "y" + y);
            canvas.drawBitmap(mCircleBp, x, y, new Paint());
        }
//        canvas.drawText(mCurrent + "˚C", mWidth / 2, mHeight / 2, mPaint);

    }

    //绘制基准线
    private void drawDatumLine(Canvas canvas) {

        //横线
        DashPathEffect effects = new DashPathEffect(new float[] {5,10}, 1);
        datumPaint.setPathEffect(effects);
        //五组虚线
        Path path=new Path();
        path.moveTo(0, centerY);
        path.lineTo(mWidth, centerY);
        canvas.drawPath(path,datumPaint);

        path.moveTo(0, centerY / 6 * 5);
        path.lineTo(mWidth, centerY / 6 * 5);
        canvas.drawPath(path,datumPaint);

        path.moveTo(0, centerY / 6 * 4);
        path.lineTo(mWidth, centerY / 6 * 4);
        canvas.drawPath(path,datumPaint);

        path.moveTo(0, centerY / 6 * 3);
        path.lineTo(mWidth, centerY / 6 * 3);
        canvas.drawPath(path,datumPaint);

        path.moveTo(0, centerY / 6 * 2);
        path.lineTo(mWidth, centerY / 6 * 2);
        canvas.drawPath(path,datumPaint);

        path.moveTo(0, centerY / 6);
        path.lineTo(mWidth, centerY / 6);
        canvas.drawPath(path,datumPaint);

        //canvas.drawLine(0, centerY, mWidth, centerY, datumPaint);
        //canvas.drawLine(0, centerY / 6 * 5, mWidth, centerY / 6 * 5, datumPaint);
//        canvas.drawLine(0, centerY / 6 * 4, mWidth, centerY / 6 * 4, datumPaint);
//        canvas.drawLine(0, centerY / 6 * 3, mWidth, centerY / 6 * 3, datumPaint);
//        canvas.drawLine(0, centerY / 6 * 2, mWidth, centerY / 6 * 2, datumPaint);
//        canvas.drawLine(0, centerY / 6 * 1, mWidth, centerY / 6 * 1, datumPaint);


        if (mFahrenheit) {
            canvas.drawText(30 + "˚F", mWidth, centerY - centerY / 21, textPaint);
            canvas.drawText(40 + "˚F", mWidth, centerY / 6 * 1 - centerY / 21, textPaint);
        } else {
            //温度
            canvas.drawText(MIN_TEMPERATURE + "˚C", mWidth, centerY - centerY / 21, textPaint);
            canvas.drawText(MAX_TEMPERATURE + "˚C", mWidth, centerY / 6 - centerY / 21, textPaint);
        }


        //竖线
        canvas.drawLine(mWidth / 5, centerY, mWidth / 5, centerY / 6, datumPaint);
        canvas.drawLine(mWidth / 5 * 2, centerY, mWidth / 5 * 2, centerY / 6, datumPaint);
        canvas.drawLine(mWidth / 5 * 3, centerY, mWidth / 5 * 3, centerY / 6, datumPaint);
        canvas.drawLine(mWidth / 5 * 4, centerY, mWidth / 5 * 4, centerY / 6, datumPaint);
        canvas.drawLine(mWidth / 5 * 5, centerY, mWidth / 5 * 5, centerY / 6, datumPaint);
        canvas.drawLine(mWidth / 5 * 6, centerY, mWidth / 5 * 6, centerY / 6, datumPaint);


    }

    //绘制路径
    private void drawPath(Canvas canvas, List<PointF> data) {
        try {
            drawBg(canvas, data);
            drawLine(canvas, data);
        } catch (Exception e) {
            if (destPoints != null) {
                //FirebaseCrash.report(new Exception("onAnimationEnd bug 329 line  Isonce" + Isonce + "destPoints " + destPoints.size()));

            } else {
               // FirebaseCrash.report(new Exception("onAnimationEnd bug 329 line  Isonce" + Isonce + "destPoints null"));

            }
            reStart();
        }


//        drawPath1(canvas);

    }


    private void drawLine(Canvas canvas, List<PointF> data) {
        Path path = new Path();

        PointF start = data.get(0);
        path.moveTo(0, start.y + offsetY);

        for (int i = 1; i < data.size() - 1; i ++) {
            PointF point = data.get(i);
            PointF point1 = data.get(i + 1);
//            L.d(TAG, "drawLine: random.nextInt(10)" + random.nextInt(10) % 10);
            //path.quadTo(point.x, point.y + offsetY, point.x, point.y + offsetY);
            path.lineTo(point.x, point.y + offsetY);
        }

//        mTempValues.add(data.get(0));
        canvas.drawPath(path, linePaint);
    }

    private void drawBg(Canvas canvas, List<PointF> data) {
        Path path = new Path();
        path.moveTo(0, centerY);
        path.lineTo(0, data.get(0).y);
        for (int i = 1; i < data.size(); i++) {
            PointF point = data.get(i);

            path.lineTo(mWidth/5*i, point.y + offsetY);
        }
        path.lineTo(data.get(data.size() - 1).x, centerY);
        canvas.drawPath(path, bgPaint);
    }


    //深复制
    private ArrayList<PointF> copyData(List<PointF> points) {
        ArrayList<PointF> data = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            PointF point = points.get(i);
            data.add(new PointF(point.x, point.y));
        }
        return data;
    }

    //De Casteljau算法
    public PointF deCasteljau(float fraction, List<PointF> points) {
        List<PointF> data = copyData(points);

        final int n = data.size();
         PointF pointF=new PointF(data.get(n-2).x+(data.get(n-1).x-data.get(n-2).x)*fraction,
                data.get(n-2).y+(data.get(n-1).y-data.get(n-2).y)*fraction);
//        for (int i = 1; i <= n; i++) {
//            for (int j = 0; j < n - i; j++) {
//                data.get(j).x = (1 - fraction) * data.get(j).x + fraction * data.get(j + 1).x;
//                data.get(j).y = (1 - fraction) * data.get(j).y + fraction * data.get(j + 1).y;
//            }
////
////            data.get(j).x = (1 - fraction) * data.get(j).x + fraction * data.get(j + 1).x;
////            data.get(j).y = (1 - fraction) * data.get(j).y + fraction * data.get(j + 1).y;
//        }
        if (data.size() > 0) {
            return pointF;

        }
        return new PointF();
    }


    int tempAnimatedValue;
    private Float mAFloat;

    public void stop() {
        mCurrentSates = Sates.STOP;
//        L.d(TAG, "stop: 111111213");
        if (mPathAnimator != null) {
//            if(mVerticalAnimator.isRunning()){
            mPathAnimator.cancel();
//            mPathAnimator.
            mPathAnimator.end();
//            }
            mPathAnimator = null;
        }


        if (mVerticalAnimator != null) {
//            if (mVerticalAnimator.isRunning()) {
            mVerticalAnimator.cancel();

            mVerticalAnimator.end();
//            }
            mVerticalAnimator = null;

        }

        if (mHorizontalAnimator != null) {
//            if (mHorizontalAnimator.isRunning()) {
            mHorizontalAnimator.cancel();

            mHorizontalAnimator.end();
            mHorizontalAnimator = null;
//            }

        }


    }

    public void start() {
//        L.d(TAG, "start: 111111213");

        initDate();

    }

    public void forwardAnimation() {
        mPathAnimator = ValueAnimator.ofInt(0, finalPoints.size());
        mPathAnimator.setInterpolator(new LinearInterpolator());
        mPathAnimator.setDuration(ANIM_DURATION);

        transformShader();
        mPathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if (mCurrentSates == Sates.STOP) {
                    return;
                }

                fraction = animation.getAnimatedFraction();
                int value= (int) animation.getAnimatedValue();
                System.out.println("fraction--"+fraction+"--AnimatedValue--"+animation.getAnimatedValue());

                try {
//                    PointF pointF = deCasteljau(fraction, points);
//                    if (pointF.x < (mWidth / 5 * 4) && (pointF.x > (mWidth / 5 * 3) || !Isonce)) {
//                        if (mCurrentSates == Sates.STOP) {
//                            return;
//                        }
//
//
//                    }
                    destPoints.add(finalPoints.get(value));
                    invalidate();

                } catch (Exception e) {
                    if (points != null) {
                        //FirebaseCrash.report(new Exception("onAnimationEnd bug 458 line  points.size()" + points.size()));

                    } else {
                        //FirebaseCrash.report(new Exception("onAnimationEnd bug 458 line  points.size() is null"));
                    }
                    reStart();
                }

            }
        });
        mPathAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Isonce = true;

                if (mCurrentSates == Sates.STOP) {
                    once = false;
                    return;
                } else {
                    once = true;


                    destPoints=finalPoints;
                    mCurrentSates = Sates.PATH;
                    invalidate();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    float tempOffsetY;

    public void verticalTranslation() {
        mVerticalAnimator = ValueAnimator.ofFloat(0, tempOffsetY);
        mVerticalAnimator.setInterpolator(new LinearInterpolator());
        mVerticalAnimator.setDuration(200);

        mVerticalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mCurrentSates == Sates.STOP) {
                    return;
                }

                float tempAnimatedValue = (float) animation.getAnimatedValue();

                offsetY = tempAnimatedValue;

                invalidate();

            }
        });

        mVerticalAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurrentSates == Sates.STOP) {
                    return;
                }
                for (int i = 0; i < destPoints.size(); i++) {
                    destPoints.get(i).y = destPoints.get(i).y + offsetY;
                }
                offsetY = 0;

                invalidate();

                mPathAnimator.setDuration(400);
                mPathAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    public void translationAnimation() {
        mHorizontalAnimator = ValueAnimator.ofInt(0, mWidth / 5);
        mHorizontalAnimator.setInterpolator(new LinearInterpolator());
        mHorizontalAnimator.setDuration(400);

        mHorizontalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                L.d(TAG, "onAnimationUpdate: 123234324");

                if (mCurrentSates == Sates.STOP) {
                    return;
                }

                tempAnimatedValue = (int) animation.getAnimatedValue();

                try {
                    for (int i = 0; i < mTempDestPoints.size(); i++) {

                        float x = mTempDestPoints.get(i).x - tempAnimatedValue;
                        destPoints.get(i).set(x, mTempDestPoints.get(i).y);

                    }

                } catch (Exception e) {
                    if (destPoints != null && mTempDestPoints != null) {
                        //FirebaseCrash.report(new Exception("onAnimationEnd bug 599 line destPoints" + destPoints.size() + "mTempDestPoints" + mTempDestPoints.size()));

                    }
                    reStart();

                }


                invalidate();

            }


        });


        mHorizontalAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
//                L.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurrentSates == Sates.STOP) {
                    return;
                }

                try {
                    if (destPoints != null) {
                        while (destPoints.size() > 0 && destPoints.get(0).x < 0) {
                            destPoints.remove(0);
                        }
                    }
                } catch (Exception e) {
                   // FirebaseCrash.report(new Exception("onAnimationEnd bug 636 line" + destPoints.size()));

                    if (destPoints == null) {
                        destPoints = new ArrayList<PointF>();
                    }
                }


                points.clear();
                if (mTemperatures.size() == 0) {
                    mTemperatures.add(mCurrentTemperature);
                }
                try {
                    if (mTemperatures.get(0) > MAX_TEMPERATURE) {
                        try {
                            mTempDestPoints = copyData(destPoints);
                            deviationalue = (int) (mTemperatures.get(0) - MAX_TEMPERATURE) + 1;
                            MAX_TEMPERATURE = MAX_TEMPERATURE + deviationalue;
                            MIN_TEMPERATURE = MIN_TEMPERATURE + deviationalue;
                            tempOffsetY = centerY / 6 * (deviationalue);
                            verticalTranslation();

                            try {
                                if (destPoints.size() != 0) {
                                    points.add(new PointF(destPoints.get(destPoints.size() - 1).x, destPoints.get(destPoints.size() - 1).y + tempOffsetY));
                                }
                            } catch (Exception e) {
                                //FirebaseCrash.report(new Exception("onAnimationEnd bug 665 line" + mCurrentSates + "destPoints" + destPoints.size()));
                            }

                            mAFloat = (MAX_TEMPERATURE - mTemperatures.get(0)) * (centerY / 6) + centerY / 6 + tempOffsetY;

                            points.add(new PointF(mWidth / 5 * 4 - mWidth / 21, mAFloat - random.nextInt(mHeight / 10)));
                            points.add(new PointF(mWidth / 5 * 4 - mWidth / 11, mAFloat - random.nextInt(mHeight / 10)));
                            points.add(new PointF(mWidth / 5 * 4, mAFloat));

                        } catch (Exception e) {
                            //FirebaseCrash.report(new Exception("onAnimationEnd bug 675 line" + mCurrentSates + "destPoints" + destPoints.size()));
                        }

                        if (mTemperatures.size() > 1) {
                            float mAFloat1 = (MAX_TEMPERATURE - mTemperatures.get(1)) * (centerY / 6) + centerY / 6;
                            points.add(new PointF(mWidth / 5 * 5, mAFloat1));
                        }

                        mVerticalAnimator.start();


                    } else if (mTemperatures.get(0) < MIN_TEMPERATURE) {

                        try {
                            mTempDestPoints = copyData(destPoints);
                            deviationalue = (int) (mTemperatures.get(0) - MIN_TEMPERATURE) - 1;
                            MAX_TEMPERATURE = MAX_TEMPERATURE + deviationalue;
                            MIN_TEMPERATURE = MIN_TEMPERATURE + deviationalue;

                            tempOffsetY = centerY / 6 * (deviationalue);
                            verticalTranslation();

                            try {
                                if (destPoints.size() != 0) {
                                    points.add(new PointF(destPoints.get(destPoints.size() - 1).x, destPoints.get(destPoints.size() - 1).y + tempOffsetY));
                                }

                            } catch (Exception e) {
                                //FirebaseCrash.report(new Exception("onAnimationEnd bug 700 line" + mCurrentSates + "destPoints" + destPoints.size()));

                            }

                            mAFloat = (MAX_TEMPERATURE - mTemperatures.get(0)) * (centerY / 6) + centerY / 6 + tempOffsetY;

                            points.add(new PointF(mWidth / 5 * 4 - mWidth / 21, mAFloat + random.nextInt(mHeight / 10)));
                            points.add(new PointF(mWidth / 5 * 4 - mWidth / 11, mAFloat - random.nextInt(mHeight / 10)));
                            points.add(new PointF(mWidth / 5 * 4, mAFloat));


                        } catch (Exception e) {
                            //FirebaseCrash.report(new Exception("onAnimationEnd bug 698 line" + mCurrentSates + "destPoints" + destPoints.size()));
                        }

                        if (mTemperatures.size() > 1) {
                            float mAFloat1 = (MAX_TEMPERATURE - mTemperatures.get(1)) * (centerY / 6) + centerY / 6;
                            points.add(new PointF(mWidth / 7 * 7, mAFloat1));
                        }

                        mVerticalAnimator.start();


                    } else {
                        //// TODO: 18/11/2016 jx
                        try {
                            if (destPoints.size() == 0) {
                                mAFloat = (MAX_TEMPERATURE - mTemperatures.get(0)) * (centerY / 6) + centerY / 6;
                                points.add(new PointF(mWidth / 5 * 4 - mWidth / 21, mAFloat - random.nextInt(mHeight / 10)));
                                points.add(new PointF(mWidth / 5 * 4 - mWidth / 11, mAFloat - random.nextInt(mHeight / 10)));
                            } else {
                                points.add(new PointF(destPoints.get(destPoints.size() - 1).x, destPoints.get(destPoints.size() - 1).y));
                                mAFloat = (MAX_TEMPERATURE - mTemperatures.get(0)) * (centerY / 6) + centerY / 6;

                                if (destPoints.get(destPoints.size() - 1).y > mAFloat) {

//                                    L.d(TAG, "onAnimationEnd: 升");
                                    points.add(new PointF(mWidth / 5 * 4 - mWidth / 21, mAFloat + random.nextInt(mHeight / 10)));
                                    points.add(new PointF(mWidth / 5 * 4 - mWidth / 11, mAFloat - random.nextInt(mHeight / 10)));

                                } else {
//                                    L.d(TAG, "onAnimationEnd: 降");

                                    points.add(new PointF(mWidth / 5 * 4 - mWidth / 21, mAFloat - random.nextInt(mHeight / 10)));
                                    points.add(new PointF(mWidth / 5 * 4 - mWidth / 11, mAFloat - random.nextInt(mHeight / 10)));
                                }

                            }

                            points.add(new PointF(mWidth / 5 * 4, mAFloat));

                        } catch (Exception e) {
                            //FirebaseCrash.report(new Exception("onAnimationEnd bug 731 line" + mCurrentSates + "destPoints" + destPoints.size()));
                        }
                        if (mTemperatures.size() > 1) {
                            float mAFloat1 = (MAX_TEMPERATURE - mTemperatures.get(1)) * (centerY / 6) + centerY / 6;
                            points.add(new PointF(mWidth / 5 * 5, mAFloat1));
                        }


                        mPathAnimator.setDuration(400);
                        mPathAnimator.start();
                    }

                    mTemperatures.remove(0);
                } catch (Exception e) {
                    //FirebaseCrash.report(new Exception("onAnimationEnd bug 745 line"));

                    reStart();
                }


            }


            @Override
            public void onAnimationCancel(Animator animation) {
//                L.d(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private boolean mFahrenheit;

    public float setTemperature(float newestTemperature, boolean fahrenheit) {
        mFahrenheit = fahrenheit;

        if (mTemperatures != null) {
            while (mTemperatures.size() > 2) {
                mTemperatures.remove(0);
            }
        } else {
            mTemperatures = new ArrayList<>();
            mTemperatures.add(37.5f);
        }
        while (newestTemperature > MAX_TEMPERATURE + 1) {
            newestTemperature--;
        }
        while (newestTemperature < MIN_TEMPERATURE - 1) {
            newestTemperature++;
        }
        
        if (mTemperatures.size() != 0) {
            mCurrentTemperature = mTemperatures.get(mTemperatures.size() - 1);
        }

        while (Math.abs(mCurrentTemperature - newestTemperature) > 0.5f) {
            if (mCurrentTemperature > newestTemperature) {
                newestTemperature += 0.5f;
            } else {
                newestTemperature -= 0.5f;
            }
        }


       // L.d(TAG, "setTemperature: newestTemperature" + newestTemperature);
        mTemperatures.add((float) (Math.round((newestTemperature) * 10)) / 10);
        return mTemperatures.get(0);
    }

    private void reStart() {
//        stop();
//        start();
//        start1();
    }


    private static final int FRAME = 1000;  // 1000帧


    /**
     * 创建Bezier点集
     *
     * @return
     */
    private ArrayList<PointF> buildBezierPoints() {
        ArrayList<PointF> pointss = new ArrayList<>();
        int order = points.size() - 1;
        float delta = 1.0f / FRAME;
        for (float t = 0; t <= 1; t += delta) {
            // Bezier点集
            pointss.add(new PointF(deCasteljauX(order, 0, t), deCasteljauY(order, 0, t)));
        }
        return pointss;
    }

    /**
     * deCasteljau算法
     *
     * @param i 阶数
     * @param j 点
     * @param t 时间
     * @return
     */
    private float deCasteljauX(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * points.get(j).x + t * points.get(j + 1).x;
        }
        return (1 - t) * deCasteljauX(i - 1, j, t) + t * deCasteljauX(i - 1, j + 1, t);
    }

    /**
     * deCasteljau算法
     *
     * @param i 阶数
     * @param j 点
     * @param t 时间
     * @return
     */
    private float deCasteljauY(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * points.get(j).y + t * points.get(j + 1).y;
        }
        return (1 - t) * deCasteljauY(i - 1, j, t) + t * deCasteljauY(i - 1, j + 1, t);
    }




    /**
     * 创建直线点集
     *
     * @return
     */

    private final  int REFRESH=50;
    private ArrayList<PointF> buildDetailPoints() {
        ArrayList<PointF> pointss = new ArrayList<>();
        for (int i=0;i<points.size()-1;i++){
            ArrayList<Float> x=divideXY(points.get(i+1).x,points.get(i).x);
            ArrayList<Float> y=divideXY(points.get(i+1).y,points.get(i).y);
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
    private ArrayList<Float> divideXY(float xy2, float xy1) {
        ArrayList<Float> arrayList=new ArrayList<>();
        for (int i=0;i<REFRESH;i++){
            arrayList.add((xy2-xy1)/REFRESH*i);
        }
        return arrayList;
    }





    private boolean isRunning() {
        return (mState & STATE_RUNNING) == STATE_RUNNING;
    }

    private boolean isReady() {
        return (mState & STATE_READY) == STATE_READY;
    }


    /**
     * 开始
     */
    public void start1() {

    }

}
