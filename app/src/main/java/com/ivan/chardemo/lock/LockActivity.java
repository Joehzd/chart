package com.ivan.chardemo.lock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ivan.chardemo.R;

/**
 * Created by a123 on 2017/5/17.
 */

public class LockActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_activity);
    }

    public void lock(View view) {
        finish();
    }
}
