package com.assignment.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yradex on 2016/11/23.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String className = bundle.getString(AlarmManager.EXTRA_CLASSNAME);
        int id = bundle.getInt(AlarmManager.EXTRA_ALARMID);

        AlarmManager.globalInitialize(context);
        AlarmManager alarmManager = new AlarmManager(context);

        if (alarmManager.getRecordById(id) == null) {
            return;
        }

        alarmManager.wakeUpByAlarm(id);

        Intent newIntent = null;
        try {
            newIntent = new Intent(context, Class.forName(className));
            newIntent.putExtra(AlarmManager.EXTRA_ALARMID, id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        context.startActivity(newIntent);
    }
}
