package com.ivan.chardemo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {



    TextView textView;
    Button button;
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


        AlertDialog.Builder dialog=new AlertDialog.Builder(this);

        LayoutInflater layoutInflater=this.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.clean_dialog_layout,null);
        button= (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点我",Toast.LENGTH_LONG).show();
            }
        });

        dialog.setView(view);
        dialog.create().show();





        SpannableString spannable = new SpannableString("温度太高了\n   哈哈哈2345");


        spannable.setSpan(new TextAppearanceSpan(this, R.style.temperature_text),
                0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new TextAppearanceSpan(this, R.style.temperature_text_small),
                spannable.length() - 7,
                spannable.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);



        Drawable drawable = getResources().getDrawable(R.drawable.temperature_oval);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //需要处理的文本，0是需要被替代的文本

        //要让图片替代指定的文字就要用ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
        //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
        spannable.setSpan(span, spannable.length()-2,spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
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
