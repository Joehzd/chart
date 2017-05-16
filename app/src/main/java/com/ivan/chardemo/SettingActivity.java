package com.ivan.chardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by a123 on 2017/5/16.
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting_activity);
        getFragmentManager().beginTransaction().add(R.id.container,new SettingFragment()).commit();

    }
}
