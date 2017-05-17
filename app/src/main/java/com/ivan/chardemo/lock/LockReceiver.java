package com.ivan.chardemo.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by a123 on 2017/5/17.
 */

public class LockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1=new Intent(context,LockActivity.class);
        context.startActivity(intent1);

    }
}
