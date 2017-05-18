package com.ivan.chardemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {



    TextView textView;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x1234:
                    chartView.setTempurature((float) Math.random()*10+35,true);
                    break;
            }

        }
    };

    ChartView chartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= (TextView) findViewById(R.id.text);
        //Spannable spannable=

        Drawable drawable = getResources().getDrawable(R.drawable.temperature_oval);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，0是需要被替代的文本
        SpannableString spannable = new SpannableString("999");
        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
        spannable.setSpan(span, 1,"999".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
//        chartView= (ChartView) findViewById(R.id.chart);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    handler.sendEmptyMessage(0x1234);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

//        Intent intent=new Intent(this,SettingActivity.class);
//
//
//        startActivity(intent);
//        chartView= (ChartView) findViewById(R.id.chart);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    handler.sendEmptyMessage(0x1234);
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //chartView.recycleResourse();
    }
}
