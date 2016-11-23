package com.assignment.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yradex on 2016/11/23.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = null;
        try {
            newIntent = new Intent(context, Class.forName(intent.getStringExtra("class")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        context.startActivity(newIntent);
    }
}
