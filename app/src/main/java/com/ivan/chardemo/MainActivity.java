package com.ivan.chardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChartView chartView= (ChartView) findViewById(R.id.chart);
        chartView.startAnim(1000);


//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        MyLineChart.right = dm.widthPixels - 35;
//        MyLineChart.gapX = (dm.heightPixels - 45) / 23;
    }
}
