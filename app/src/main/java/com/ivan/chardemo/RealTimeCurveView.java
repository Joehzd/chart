//package com.ivan.chardemo;
//
//import android.graphics.Color;
//import android.os.Build;
//import android.view.View;
//
///**
// * Created by ivan on 2017/5/7.
// */
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.DrawFilter;
//import android.graphics.LinearGradient;
//import android.graphics.Paint;
//import android.graphics.PaintFlagsDrawFilter;
//import android.graphics.Path;
//import android.graphics.PointF;
//import android.graphics.Rect;
//import android.graphics.Shader;
//import android.util.AttributeSet;
//import android.view.animation.Interpolator;
//import android.view.animation.LinearInterpolator;
//
//import java.text.SimpleDateFormat;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//
//public class RealTimeCurveView extends View
//{
//    private int A;
//    private int B;
//    private int C;
//    private int D;
//    private int E;
//    private int F;
//    private int G;
//    private int H;
//    private int I;
//    private int J;
//    private int K;
//    private int L;
//    private int M;
//    private Rect rect;
//    private PointF pointF;
//    private SimpleDateFormat simpleDateFormat;
//    private float Q;
//    private float R;
//    private float S;
//    private final LinkedList<Double> linkedList = new LinkedList();
//    private Bitmap bitmap;
//    private int V;
//    private int W;
//    private Resources resources;
//    private boolean aa;
//    private final Random random = new Random();
//    private RealTime realTime;
//    private Paint paint;
//    private float ae;
//    private float af;
//    private PointF pointF1;
//    private PointF pointF2;
//    private double ai;
//    private MoreDateChild aj;
//    private MoreDate ak;
//    private Paint paint1;
//    private Paint paint2;
//    private Paint paint3;
//    private Paint paint4;
//    private Paint paint5;
//    private Paint paint6;
//    private int h;
//    private int i;
//    private Shader shader;
//    private Shader shader1;
//    private Path path;
//    private Path path1;
//    private Context context;
//    private Rect rect1;
//    private DrawFilter drawFilter;
//    private final List<PointF> linkedList1 = new LinkedList();
//    private long r;
//    private long s;
//    private Interpolator interpolator;
//    private int u;
//    private int v;
//    private int w;
//    private int x;
//    private int y;
//    private int z;
//
//    public RealTimeCurveView(Context paramContext)
//    {
//        super(paramContext);
//        init(paramContext);
//    }
//
//    public RealTimeCurveView(Context paramContext, AttributeSet paramAttributeSet)
//    {
//        super(paramContext, paramAttributeSet);
//        init(paramContext);
//    }
//
//    public RealTimeCurveView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
//    {
//        super(paramContext, paramAttributeSet, paramInt);
//        init(paramContext);
//    }
//
//    public RealTimeCurveView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
//    {
//        super(paramContext, paramAttributeSet, paramInt1, paramInt2);
//        init(paramContext);
//    }
//
//    @SuppressLint({"NewApi"})
//    private void init(Context paramContext)
//    {
//        if ((Build.VERSION.SDK_INT >= 11) && (!paint4.a(this))) {
//            setLayerType(1, null);
//        }
//        this.context = paramContext;
//        this.resources = getResources();
//        this.drawFilter = new PaintFlagsDrawFilter(0, 3);
//        this.interpolator = new LinearInterpolator();
//        this.C = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 30.0F);
//        this.D = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 30.0F);
//        this.E = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 10.0F);
//        this.I = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 4.0F);
//        this.J = (this.I / 2);
//        this.K = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 5.0F);
//        this.L = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 5.0F);
//        this.M = com.duapps.coolermaster.cpucooler.utils.n.a(this.context, 3.0F);
//        initDate();
//        this.aj = paint2.a();
//        this.simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//    }
//
//    private void drawLines(Canvas paramCanvas)
//    {
//        for (int i = 0; i < 6; i++)
//        {
//            int j = this.H - i* this.B;
//            this.paint5.setColor(654311423);
//            paramCanvas.drawLine(0.0F, j, this.h, j, this.paint5);
//        }
//    }
//
//    private void a(MoreDate paramb)
//    {
//        paramb.b();
//        double d1 = this.aj.d();
//        if (this.linkedList.isEmpty())
//        {
//            this.r = 0L;
//            return;
//        }
//        double d2 = ((Double)this.linkedList.getLast()).doubleValue();
//        long l1 = System.currentTimeMillis() - paint3.e();
//        if ((l1 > 0L) && (l1 <= 45000L)) {
//            this.ak = new MoreDateChild3(d2, d1);
//        }
//        for (;;)
//        {
//            this.ak.a();
//            return;
//            Double localDouble = Double.valueOf(Math.abs(d1 - d2));
//            if ((localDouble.doubleValue() >= 0.0D) && (localDouble.doubleValue() < 1.0D))
//            {
//                this.ak = new MoreDateChild(d2, d1);
//            }
//            else
//            {
//                if ((localDouble.doubleValue() < 1.0D) || (localDouble.doubleValue() > 5.0D)) {
//                    break;
//                }
//                this.ak = new MoreDateChild2(d2, d1);
//            }
//        }
//        if (d1 > d2) {}
//        for (int i1 = 5;; i1 = -5)
//        {
//            this.ak = new MoreDateChild2(d2, d2 + i1);
//            break;
//        }
//    }
//
//    private void a(List<Double> paramList)
//    {
//        this.linkedList1.clear();
//        float f1 = this.F / 7.0F;
//        int i1 = 0;
//        double d1;
//        PointF localPointF;
//        float f2;
//        label71:
//        float f4;
//        float f5;
//        if (i1 < paramList.size())
//        {
//            d1 = ((Double)paramList.get(i1)).doubleValue();
//            localPointF = new PointF();
//            f2 = f1 * i1;
//            if (i1 == 0)
//            {
//                localPointF.x = 0.0F;
//                float f3 = (float)(d1 - this.x) / this.z * this.G;
//                f4 = this.G - f3 + this.C;
//                f5 = this.H - this.I;
//                if (f4 < f5) {
//                    break label184;
//                }
//            }
//        }
//        for (;;)
//        {
//            localPointF.y = f5;
//            if (i1 == 6)
//            {
//                this.pointF = localPointF;
//                this.ai = d1;
//            }
//            this.linkedList1.add(localPointF);
//            i1++;
//            break;
//            localPointF.x = f2;
//            break label71;
//            return;
//            label184:
//            f5 = f4;
//        }
//    }
//
//    private void b(Canvas paramCanvas)
//    {
//        if (this.pointF != null)
//        {
//            this.paint.setAlpha((int)(255.0F * this.ae));
//            paramCanvas.drawBitmap(getCircleBitmap(), this.V - this.W / 2, this.pointF.y - this.W / 2 - this.af, this.paint);
//        }
//    }
//
//    private void b(List<Double> paramList)
//    {
//        if ((paramList == null) || (paramList.isEmpty())) {
//            return;
//        }
//        do
//        {
//            int i1 = 0;
//            while (i1 < paramList.size())
//            {
//                int i2 = ((Double)paramList.get(i1)).intValue();
//                if (i1 == 0)
//                {
//                    this.v = i2;
//                    this.w = i2;
//                    i1++;
//                }
//                else
//                {
//                    if (this.v < i2) {}
//                    for (int i3 = this.v;; i3 = i2)
//                    {
//                        this.v = i3;
//                        if (this.w > i2) {
//                            i2 = this.w;
//                        }
//                        this.w = i2;
//                        break;
//                    }
//                }
//            }
//            this.y = (1 + this.w);
//            this.x = (-1 + this.v);
//            this.z = (this.y - this.x);
//        } while (this.z >= 5);
//        this.y += (5 - this.z) / 2;
//        this.x = (-5 + this.y);
//        this.z = 5;
//    }
//
//    private void initDate()
//    {
//        this.paint1 = new Paint(1);
//        this.paint1.setDither(true);
//        this.paint1.setFilterBitmap(true);
//        this.paint1.setStyle(Paint.Style.FILL);
//        this.paint2 = new Paint(1);
//        this.paint2.setDither(true);
//        this.paint2.setFilterBitmap(true);
//        this.paint2.setStyle(Paint.Style.STROKE);
//        this.paint2.setStrokeCap(Paint.Cap.ROUND);
//        this.paint2.setStrokeJoin(Paint.Join.ROUND);
//        this.paint2.setStrokeWidth(this.I);
//        this.paint3 = new Paint(1);
//        this.paint3.setTextAlign(Paint.Align.CENTER);
//        this.paint3.setTextSize(this.E);
//        this.paint3.setColor(Color.BLACK);
//        this.paint4 = new Paint(1);
//        this.paint4.setTextAlign(Paint.Align.RIGHT);
//        this.paint4.setTextSize(this.E);
//        this.paint4.setColor(Color.BLUE);
//        this.paint5 = new Paint(1);
//        this.paint5.setColor(Color.RED);
//        this.paint5.setStyle(Paint.Style.STROKE);
//        this.paint5.setStrokeWidth(1.0F);
//        this.paint6 = new Paint(this.paint5);
//        this.paint = new Paint();
//        this.pointF1 = new PointF();
//        this.pointF2 = new PointF();
//    }
//
//    private void drawLineVertical(Canvas paramCanvas)
//    {
//        for (int i1 = 1; i1 < 7; i1++)
//        {
//            int i2 = i1 * this.A;
//            this.rect.left = i2;
//            this.rect.right = (i2 + 1);
//            paramCanvas.drawRect(this.rect, this.paint6);
//        }
//    }
//
//    private void c(List<PointF> paramList)
//    {
//        if ((paramList == null) || (paramList.size() == 0)) {
//            return;
//        }
//        if ((this.path1 == null) || (this.path == null))
//        {
//            this.path = new Path();
//            this.path1 = new Path();
//        }
//        this.path.reset();
//        this.path1.reset();
//        int i1 = this.h;
//        int i2 = 0;
//        int i3 = i1;
//        PointF localPointF1;
//        PointF localPointF2;
//        if (i2 < -1 + paramList.size())
//        {
//            float f1 = ((PointF)paramList.get(i2)).x;
//            float f2 = ((PointF)paramList.get(i2)).y;
//            if (i2 == 0)
//            {
//                this.path1.moveTo(f1, f2);
//                this.path.moveTo(f1, f2);
//            }
//            localPointF1 = (PointF)paramList.get(i2);
//            localPointF2 = (PointF)paramList.get(i2 + 1);
//            if (i2 != -2 + paramList.size()) {
//                for (int i4 = (int)localPointF2.x;; i4 = i3)
//                {
//                    float f3 = (localPointF1.x + localPointF2.x) / 2.0F;
//                    this.pointF1.y = localPointF1.y;
//                    this.pointF1.x = f3;
//                    this.pointF2.y = localPointF2.y;
//                    this.pointF2.x = f3;
//                    this.path1.cubicTo(this.pointF1.x, this.pointF1.y + this.J, this.pointF2.x, this.pointF2.y + this.J, localPointF2.x, localPointF2.y + this.J);
//                    this.path.cubicTo(this.pointF1.x, this.pointF1.y, this.pointF2.x, this.pointF2.y, localPointF2.x, localPointF2.y);
//                    i2++;
//                    i3 = i4;
//                    this.path1.lineTo(i3, this.H);
//                    this.path1.lineTo(0.0F, this.H);
//                    this.path1.close();
//                    return;
//                }
//            }
//        }
//
//
//    }
//
//    private void d()
//    {
//        if ((this.paint1 == null) || (this.paint2 == null)) {
//            initDate();
//        }
//        this.paint1.setShader(this.shader);
//        this.paint2.setShader(this.shader1);
//    }
//
//    private void d(Canvas paramCanvas)
//    {
//        paramCanvas.drawText(this.simpleDateFormat.format(Long.valueOf(System.currentTimeMillis())), this.V, this.i - this.D / 3, this.paint3);
//    }
//
//    private void shader()
//    {
//        this.shader = new LinearGradient(0.0F, this.C, 0.0F, this.H, -1725052161, 4880127, Shader.TileMode.CLAMP);
//        this.shader1 = new LinearGradient(0.0F, this.C, 0.0F, this.H, -13369410, -14497025, Shader.TileMode.CLAMP);
//    }
//
//    private void e(Canvas paramCanvas)
//    {
//        boolean bool = paint3.d();
//        int i1 = 0;
//        int i2;
//        int i3;
//        if (i1 < 2)
//        {
//            i2 = this.x;
//            if (i1 != 1) {
//                break label123;
//            }
//            i3 = 5;
//            i2 = this.y;
//        }
//        for (;;)
//        {
//            int i4 = this.H - i3 * this.B;
//            if (!bool) {
//                i2 = Math.round(32 + i2 * 9 / 5);
//            }
//            paramCanvas.drawText(String.valueOf(i2) + getUnitString(), this.h - this.K, i4 - this.L, this.paint4);
//            i1++;
//            break;
//            return;
//            label123:
//            i3 = 0;
//        }
//    }
//
//    private void f()
//    {
//        LinkedList localLinkedList = this.aj.deal();
//        if ((localLinkedList == null) || (localLinkedList.isEmpty())) {
//            return;
//        }
//        int i1 = localLinkedList.size();
//        double d1 = this.random.nextInt(3) - 1.5D;
//        double d2 = ((Double)localLinkedList.get(i1 - 2)).doubleValue();
//        Double localDouble = Double.valueOf(d2 + d1);
//        this.linkedList.add(localDouble);
//        long l1 = System.currentTimeMillis() - paint3.e();
//        if ((l1 > 0L) && (l1 <= 45000L)) {
//            this.ak = new MoreDateChild3(localDouble.doubleValue(), d2);
//        }
//        for (;;)
//        {
//            this.ak.a();
//            for (int i2 = 0; i2 < 7; i2++) {
//                this.ak.deal();
//            }
//            if (Math.abs(d1) <= 1.0D) {
//                this.ak = new MoreDateChild(localDouble.doubleValue(), d2);
//            } else {
//                this.ak = new MoreDateChild2(localDouble.doubleValue(), d2);
//            }
//        }
//        b(this.linkedList);
//    }
//
//    private void f(Canvas paramCanvas)
//    {
//        int i1 = paramCanvas.saveLayer(0.0F, 0.0F, this.h, this.i, this.paint2, 31);
//        paramCanvas.clipRect(this.rect1);
//        paramCanvas.translate(this.Q, this.R);
//        paramCanvas.drawPath(this.path, this.paint2);
//        paramCanvas.drawPath(this.path1, this.paint1);
//        paramCanvas.restoreToCount(i1);
//        long l1 = System.currentTimeMillis() - this.r;
//        if (l1 < 1200L) {}
//        for (this.u = ((int)(this.V * this.interpolator.getInterpolation((float)l1 / 1200.0F)));; this.u = ((int)(this.V + this.S)))
//        {
//            this.rect1.right = this.u;
//            return;
//        }
//    }
//
//    private void g()
//    {
//        this.ak.deal();
//        if (this.linkedList.isEmpty()) {
//            this.r = 0L;
//        }
//        Double localDouble;
//        do
//        {
//            return;
//            localDouble = (Double)this.linkedList.getLast();
//            if (localDouble.doubleValue() >= this.y)
//            {
//                int i2 = (int)Math.ceil(localDouble.doubleValue() - this.y);
//                this.af = (i2 / this.z * this.G);
//                this.y = (i2 + this.y);
//                this.x = (i2 + this.x);
//                return;
//            }
//        } while (localDouble.doubleValue() > this.x);
//        int i1 = (int)Math.ceil(this.x - localDouble.doubleValue());
//        this.af = (-i1 / this.z * this.G);
//        this.y -= i1;
//        this.x -= i1;
//    }
//
//    private Bitmap getCircleBitmap()
//    {
//        if (this.bitmap == null)
//        {
//            this.bitmap = BitmapFactory.decodeResource(getResources(), 2130837687);
//            this.W = this.bitmap.getWidth();
//        }
//        return this.bitmap;
//    }
//
//    private String getUnitString()
//    {
//        String str = this.context.getString(2131099925);
//        if (!paint3.d()) {
//            str = this.context.getString(2131099926);
//        }
//        return str;
//    }
//
//    private void h()
//    {
//        if ((this.h == 0) || (this.i == 0))
//        {
//            this.h = com.duapps.coolermaster.cpucooler.utils.n.a(this.context);
//            this.i = this.resources.getDimensionPixelOffset(2131165240);
//        }
//        this.F = this.h;
//        this.H = (this.i - this.D);
//        this.G = (this.H - this.C);
//        this.B = (this.G / 5);
//        this.A = (this.F / 7);
//        this.V = (this.F - this.A);
//        this.rect = new Rect(0, this.H, 1, this.H + this.M);
//    }
//
//    public void a()
//    {
//        this.aa = true;
//        this.r = 0L;
//        this.linkedList.clear();
//        postInvalidate();
//    }
//
//    public void b()
//    {
//        this.aa = false;
//    }
//
//    protected void onDraw(Canvas paramCanvas)
//    {
//        paramCanvas.setDrawFilter(this.drawFilter);
//        super.onDraw(paramCanvas);
//        if (!aa) {
//            return;
//        }
//        if (this.r == 0L)
//        {
//            this.r = System.currentTimeMillis();
//            this.s = System.currentTimeMillis();
//            h();
//            f();
//            a(this.linkedList);
//            c(this.linkedList1);
//        }
//        if ((this.shader == null) || (this.shader1 == null))
//        {
//            shader();
//            d();
//        }
//        if (System.currentTimeMillis() - this.s >= 1200L)
//        {
//            com.b.a.n localn = com.b.a.n.b(new float[] { 0.0F, 1.0F });
//            localn.c(1000L);
//            localn.a(new context.b()
//            {
//                public void a(com.b.a.n paramAnonymousn)
//                {
//                    float f1 = ((Float)paramAnonymousn.m()).floatValue();
//                    RealTimeCurveView.a(RealTimeCurveView.this, f1 * -RealTimeCurveView.a(RealTimeCurveView.this));
//                    RealTimeCurveView.b(RealTimeCurveView.this, -RealTimeCurveView.b(RealTimeCurveView.this) * (1.0F - f1));
//                    RealTimeCurveView localRealTimeCurveView1 = RealTimeCurveView.this;
//                    float f2 = RealTimeCurveView.a(RealTimeCurveView.this);
//                    if (f1 >= 0.5D) {}
//                    for (float f3 = f1 - 1.0F;; f3 = -f1)
//                    {
//                        RealTimeCurveView.c(localRealTimeCurveView1, f3 * f2);
//                        float f4 = 2.5F * f1;
//                        RealTimeCurveView localRealTimeCurveView2 = RealTimeCurveView.this;
//                        if (f4 > 1.0F) {
//                            f4 = 1.0F;
//                        }
//                        RealTimeCurveView.d(localRealTimeCurveView2, 1.0F - f4);
//                        return;
//                    }
//                }
//            });
//            localn.a(new shader()
//            {
//                public void a(RealTime paramAnonymousa)
//                {
//                    super.a(paramAnonymousa);
//                    RealTimeCurveView.a(RealTimeCurveView.this, System.currentTimeMillis());
//                    if ((RealTimeCurveView.c(RealTimeCurveView.this) != null) && (RealTimeCurveView.d(RealTimeCurveView.this) > 0.0D)) {
//                        RealTimeCurveView.c(RealTimeCurveView.this).a(RealTimeCurveView.d(RealTimeCurveView.this));
//                    }
//                }
//
//                public void b(RealTime paramAnonymousa)
//                {
//                    super.b(paramAnonymousa);
//                    RealTimeCurveView.a(RealTimeCurveView.this, 0.0F);
//                    RealTimeCurveView.e(RealTimeCurveView.this, 0.0F);
//                    if (RealTimeCurveView.e(RealTimeCurveView.this).isEmpty())
//                    {
//                        RealTimeCurveView.b(RealTimeCurveView.this, 0L);
//                        return;
//                    }
//                    RealTimeCurveView.e(RealTimeCurveView.this).removeFirst();
//                    RealTimeCurveView.f(RealTimeCurveView.this);
//                    RealTimeCurveView.a(RealTimeCurveView.this, RealTimeCurveView.e(RealTimeCurveView.this));
//                    RealTimeCurveView.b(RealTimeCurveView.this, RealTimeCurveView.g(RealTimeCurveView.this));
//                    RealTimeCurveView.b(RealTimeCurveView.this, -RealTimeCurveView.b(RealTimeCurveView.this));
//                }
//            });
//            localn.a();
//        }
//        e(paramCanvas);
//        drawLines(paramCanvas);
//        drawLineVertical(paramCanvas);
//        d(paramCanvas);
//        if ((this.linkedList1 != null) && (this.linkedList1.size() > 1)) {
//            f(paramCanvas);
//        }
//        b(paramCanvas);
//        postInvalidate();
//    }
//
//    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
//    {
//        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
//        this.h = paramInt1;
//        this.i = paramInt2;
//        this.rect1 = new Rect(0, 0, this.u, this.i);
//    }
//
//    public void setCpuStatusChangeListener( RealTime parama)
//    {
//        this.realTime = parama;
//    }
//
//    public static abstract interface RealTime
//    {
//        public abstract void a(double paramDouble);
//    }
//
//    private abstract class MoreDate
//    {
//        final double[][] a = { { 1.2D, -1.5D, -0.6D, -0.5D }, { 1.2D, 0.5D, -0.5D, 0.8D }, { -0.1D, -0.4D, 0.5D, 0.2D }, { -0.7D, 0.2D, 0.1D, 0.3D } };
//        final double[][] b = { { 0.18D, 0.25D, 0.15D, 0.3D }, { 0.15D, 0.2D, 0.25D, 0.35D }, { 0.5D, 0.2D, 0.15D, 0.05D } };
//        final double[][] c = { { 0.1D, -0.07D, 0.15D, 0.05D }, { 0.1D, 0.2D, 0.15D, 0.075D } };
//        final int d = this.a.length;
//        final int e = this.b.length;
//        final int f = this.c.length;
//        protected final double g;
//        double h;
//        int i;
//        final LinkedList<Double> j = new LinkedList();
//
//        MoreDate(double paramDouble1, double paramDouble2)
//        {
//            this.g = paramDouble2;
//            this.i = 0;
//            this.h = paramDouble1;
//            this.j.clear();
//        }
//
//        void a() {}
//
//        void b() {}
//
//        abstract void deal();
//    }
//
//    private class MoreDateChild
//            extends MoreDate
//    {
//        MoreDateChild(double paramDouble1, double paramDouble2)
//        {
//            super(paramDouble1, paramDouble2);
//            for (double d : this.a[RealTimeCurveView.h(RealTimeCurveView.this).nextInt(this.d)]) {
//                this.j.add(Double.valueOf(d));
//            }
//        }
//
//        void deal()
//        {
//            if (this.i < 4)
//            {
//                this.h = (((Double)this.j.remove(RealTimeCurveView.h(RealTimeCurveView.this).nextInt(this.j.size()))).doubleValue() + this.h);
//                RealTimeCurveView.e(RealTimeCurveView.this).add(Double.valueOf(this.h));
//            }
//            for (;;)
//            {
//                this.i = (1 + this.i);
//                return;
//                RealTimeCurveView.e(RealTimeCurveView.this).add(Double.valueOf(this.g));
//                RealTimeCurveView.a(RealTimeCurveView.this, this);
//            }
//        }
//    }
//
//    private class MoreDateChild2
//            extends MoreDate
//    {
//        private final double m;
//
//        MoreDateChild2(double paramDouble1, double paramDouble2)
//        {
//            super(paramDouble1, paramDouble2);
//            this.m = (paramDouble2 - paramDouble1);
//            for (double d : this.b[RealTimeCurveView.h(RealTimeCurveView.this).nextInt(this.e)]) {
//                this.j.add(Double.valueOf(d));
//            }
//        }
//
//        void deal()
//        {
//            if (this.i < 4)
//            {
//                double d = ((Double)this.j.remove(RealTimeCurveView.h(RealTimeCurveView.this).nextInt(this.j.size()))).doubleValue();
//                this.h += d * this.m;
//                RealTimeCurveView.e(RealTimeCurveView.this).add(Double.valueOf(this.h));
//            }
//            for (;;)
//            {
//                this.i = (1 + this.i);
//                return;
//                RealTimeCurveView.e(RealTimeCurveView.this).add(Double.valueOf(this.g));
//                RealTimeCurveView.a(RealTimeCurveView.this, this);
//            }
//        }
//    }
//
//    private class MoreDateChild3
//            extends MoreDate
//    {
//        MoreDateChild3(double paramDouble1, double paramDouble2)
//        {
//            super(paramDouble1, paramDouble2);
//            for (double d : this.c[RealTimeCurveView.h(RealTimeCurveView.this).nextInt(this.f)]) {
//                this.j.add(Double.valueOf(d));
//            }
//        }
//
//        void deal()
//        {
//            if (this.i < 4)
//            {
//                double d = ((Double)this.j.remove(RealTimeCurveView.h(RealTimeCurveView.this).nextInt(this.j.size()))).doubleValue();
//                this.h -= d;
//                RealTimeCurveView.e(RealTimeCurveView.this).add(Double.valueOf(this.h));
//            }
//            for (;;)
//            {
//                this.i = (1 + this.i);
//                return;
//                RealTimeCurveView.e(RealTimeCurveView.this).add(Double.valueOf(this.g));
//                RealTimeCurveView.a(RealTimeCurveView.this, this);
//            }
//        }
//    }
//}
//
