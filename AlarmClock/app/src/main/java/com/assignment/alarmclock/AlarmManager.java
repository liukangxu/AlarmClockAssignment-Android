package com.assignment.alarmclock;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by yradex on 2016/11/23.
 */

public final class AlarmManager {

    private static AlarmManager instance = null;

    private Context context;

    private AlarmManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static AlarmManager getInstance(Context context) {
        if (instance == null) {
            instance = new AlarmManager(context);
        }
        return instance;
    }

    /**
     * Returns a copy of the original AlarmRecordList.
     *
     * @return A copy of the original AlarmRecordList.
     */
    public List<Record> getAlarmRecordList() {
        return RecordQueueManager.instance.getAlarmRecordList();
    }

    /**
     * Returns a copy of the original CountDownRecordList.
     *
     * @return A copy of the original CountDownRecordList.
     */
    public List<Record> getCountDownRecordList() {
        return RecordQueueManager.instance.getTimerRecordList();
    }

    /**
     * Returns a copy of the original AnniversaryRecordQueue.
     *
     * @return A copy of the original AnniversaryRecordQueue.
     */
    public List<Record> getAnniversaryRecordQueue() {
        return RecordQueueManager.instance.getAnniversaryRecordList();
    }

    public void insertAlarmRecord(Record r) {
        RecordQueueManager.instance.insertAlarmRecord(r);
    }

    public void insertCountDownRecord(Record r) {
        RecordQueueManager.instance.insertTimerRecord(r);
    }

    public void insertAnniversaryRecord(Record r) {
        RecordQueueManager.instance.insertAnniversaryRecord(r);
    }

    public boolean removeAlarmRecord(Record r) {
        return RecordQueueManager.instance.removeAlarmRecord(r);
    }

    public boolean removeCountDownRecord(Record r) {
        return RecordQueueManager.instance.removeTimerRecord(r);
    }

    public boolean removeAnniversaryRecord(Record r) {
        return RecordQueueManager.instance.removeAnniversaryRecord(r);
    }

    public void invokeAfterFiveSeconds(String activityName) {
        Intent intent = new Intent(this.context, AlarmBroadcastReceiver.class);
        intent.putExtra("class", activityName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);

        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
