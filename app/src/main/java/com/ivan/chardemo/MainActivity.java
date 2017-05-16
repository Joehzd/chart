package com.ivan.chardemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
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

        Intent intent=new Intent(this,SettingActivity.class);


        startActivity(intent);
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
        chartView.recycleResourse();
    }
}
