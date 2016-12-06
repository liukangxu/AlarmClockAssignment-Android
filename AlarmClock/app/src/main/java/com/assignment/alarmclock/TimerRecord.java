package com.assignment.alarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by yradex on 2016/12/6.
 */

class TimerRecord implements Record {

    private int id;
    private Calendar calendar;

    TimerRecord(int hours, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);
        this.calendar = calendar;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Calendar getNextTriggerTime() {
        return calendar;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Class activityToHandleThisAlarm() {
        return null;
    }

    @Override
    public RecordHandler getHandler() {
        return new RecordHandler() {
            @Override
            public void handle(Context context, int id) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                Intent intent = new Intent(context, TimerActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification noti = new Notification.Builder(context)
                        .setContentTitle("计时结束")
                        .setContentText("计时结束")
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                notificationManager.notify(0, noti);

                AlarmManager alarmManager = new AlarmManager(context);
                if (!alarmManager.getRecordList(RecordType.TIMER).isEmpty()) {
                    alarmManager.removeRecord(alarmManager.getRecordList(RecordType.TIMER).get(0).getId());
                }
            }
        };
    }

}
