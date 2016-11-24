package com.assignment.alarmclock;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by yradex on 2016/11/23.
 */

final class AlarmManager {

    static final String EXTRA_CLASSNAME = "className";
    static final String EXTRA_ALARMID = "alarmId";
    static private final String ACTION_WAKEUP = "com.assignment.alarmclock.ACTION_WAKEUP";
    static private boolean initialized = false;
    private Context context;

    AlarmManager(Context context) {
        this.context = context;
    }

    static void globalInitialize(Context context) {
        if (initialized) {
            return;
        }
        initialized = true;
        // TODO
    }

    int getUniqueId(RecordType type) {
        return RecordManager.instance.getUniqueId(type);
    }

    /**
     * Returns a copy of the original AlarmRecordList.
     *
     * @return A copy of the original AlarmRecordList.
     */
    List<Record> getAlarmRecordList() {
        return RecordManager.instance.getAlarmRecordList();
    }

    /**
     * Returns a copy of the original CountDownRecordList.
     *
     * @return A copy of the original CountDownRecordList.
     */
    List<Record> getCountDownRecordList() {
        return RecordManager.instance.getTimerRecordList();
    }

    /**
     * Returns a copy of the original AnniversaryRecordQueue.
     *
     * @return A copy of the original AnniversaryRecordQueue.
     */
    List<Record> getAnniversaryRecordList() {
        return RecordManager.instance.getAnniversaryRecordList();
    }

    void insertAlarmRecord(Record r) {
        RecordManager.instance.insertAlarmRecord(r);
        registerAlarm(r);
    }

    void insertTimerRecord(Record r) {
        RecordManager.instance.insertTimerRecord(r);
        registerAlarm(r);
    }

    void insertAnniversaryRecord(Record r) {
        RecordManager.instance.insertAnniversaryRecord(r);
        registerAlarm(r);
    }

    void removeAlarmRecord(Record r) {
        RecordManager.instance.removeAlarmRecord(r);
        cancelAlarm(r);
    }

    void removeTimerRecord(Record r) {
        RecordManager.instance.removeTimerRecord(r);
        cancelAlarm(r);
    }

    void removeAnniversaryRecord(Record r) {
        RecordManager.instance.removeAnniversaryRecord(r);
        cancelAlarm(r);
    }

    @Deprecated
    void invokeAfterFiveSeconds(String activityName) {
        Intent intent = new Intent(ACTION_WAKEUP);
        intent.putExtra("class", activityName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);

        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    void wakeUpByAlarm(int id) {
        Record record = RecordManager.instance.getRecordById(id);
        registerAlarm(record);
    }

    private void cancelAlarm(Record record) {
        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        Log.d("AlarmManager", "Removing Alarm: " + Integer.toString(record.getId()));
        alarmManager.cancel(PendingIntent.getBroadcast(this.context, record.getId(), new Intent(ACTION_WAKEUP), 0));
        Log.d("AlarmManager", "Removed Alarm: " + Integer.toString(record.getId()));
    }

    private void registerAlarm(Record record) {
        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);

        if (record != null && record.isActive()) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_CLASSNAME, record.activityToHandleThisAlarm().getName());
            bundle.putInt(EXTRA_ALARMID, record.getId());

            Intent intent = new Intent(ACTION_WAKEUP);
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, record.getId(), intent, 0);

            alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, record.getNextTriggerTime().getTimeInMillis(), pendingIntent);

            Log.d("AlarmManager", "set alarm " + record.getId());
        }
    }

    Record getRecordById(int id) {
        return RecordManager.instance.getRecordById(id);
    }
}
