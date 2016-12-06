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

        int id = intent.getIntExtra(AlarmManager.EXTRA_ALARMID, 0);

        AlarmManager.globalInitialize(context);
        AlarmManager alarmManager = new AlarmManager(context);

        if (alarmManager.getRecordById(id) == null) {
            return;
        }

        alarmManager.wakeUpByAlarm(id);

        Record record = alarmManager.getRecordById(id);

        if (record.getHandler() != null) {
            record.getHandler().handle(context, id);
        }

        if (record.activityToHandleThisAlarm() != null) {
            Intent newIntent = new Intent(context, record.activityToHandleThisAlarm());
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.putExtra(AlarmManager.EXTRA_ALARMID, id);
            context.startActivity(newIntent);
        }
    }
}
