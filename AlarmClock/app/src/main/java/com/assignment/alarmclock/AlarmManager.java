package com.assignment.alarmclock;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yradex on 2016/11/23.
 */

public final class AlarmManager {

    static final String EXTRA_CLASSNAME = "className";
    static final String EXTRA_ALARMID = "alarmId";
    private static final String ACTION_WAKEUP = "com.assignment.alarmclock.ACTION_WAKEUP";
    private static boolean initialized = false;
    private Context context;

    /**
     * 取得一个该类的实例。
     *
     * @param context 调用者所在的 context
     */
    public AlarmManager(Context context) {
        this.context = context;
    }

    static void globalInitialize(Context context) {
        if (initialized) {
            return;
        }
        initialized = true;

        AlarmManager alarmManager = new AlarmManager(context);
        alarmManager.initReadFromDatabase();
    }

    /**
     * 取得所有闹钟记录的列表。不保证该列表中元素的顺序。
     * <p>
     * 在这个列表中进行修改不会影响到后台服务的工作。
     *
     * @return 所有闹钟记录的列表
     */
    public List<Record> getAlarmRecordList() {
        return RecordManager.instance.getAlarmRecordList();
    }

    /**
     * 取得所有定时器记录的列表。不保证该列表中元素的顺序。
     * <p>
     * 在这个列表中进行修改不会影响到后台服务的工作。
     *
     * @return 所有定时器记录的列表
     */
    public List<Record> getCountDownRecordList() {
        return RecordManager.instance.getTimerRecordList();
    }

    /**
     * 取得所有定时器记录的列表。不保证该列表中元素的顺序。
     * <p>
     * 在这个列表中进行修改不会影响到后台服务的工作。
     *
     * @return 所有定时器记录的列表
     */
    public List<Record> getAnniversaryRecordList() {
        return RecordManager.instance.getAnniversaryRecordList();
    }

    private void insertRecordWithId(Record r) {
        int id = r.getId();
        switch (RecordManager.instance.getRecordTypeById(id)) {
            case ALARM:
                RecordManager.instance.insertAlarmRecord(r);
                registerAlarm(r);
            case TIMER:
                RecordManager.instance.insertTimerRecord(r);
                registerAlarm(r);
            case ANNIVERSARY:
                RecordManager.instance.insertAnniversaryRecord(r);
                registerAlarm(r);
            case EOF:
                break;
        }
    }

    /**
     * 向后台服务插入一个闹钟记录。记录的 id 将会被后台服务自动设定。
     * <p>
     * 该记录将被安排定时触发。
     *
     * @param r 需要插入的闹钟记录
     */
    public void insertAlarmRecord(Record r) {
        r.setId(RecordManager.instance.getUniqueId(RecordType.ALARM));
        Record old = RecordManager.instance.insertAlarmRecord(r);
        if (old != null) {
            cancelAlarm(old);
            removeRecordFromDatabase(old);
        }
        registerAlarm(r);
        writeRecordToDatabase(r);
    }

    /**
     * 向后台服务插入一个定时器记录。记录的 id 将会被后台服务自动设定。
     * <p>
     * 该记录将被安排定时触发。
     *
     * @param r 需要插入的定时器记录
     */
    public void insertTimerRecord(Record r) {
        r.setId(RecordManager.instance.getUniqueId(RecordType.TIMER));
        Record old = RecordManager.instance.insertTimerRecord(r);
        if (old != null) {
            cancelAlarm(old);
            removeRecordFromDatabase(old);
        }
        registerAlarm(r);
        writeRecordToDatabase(r);
    }

    /**
     * 向后台服务插入一个纪念日记录。记录的 id 将会被后台服务自动设定。
     * <p>
     * 该记录将被安排定时触发。
     *
     * @param r 需要插入的纪念日记录
     */
    public void insertAnniversaryRecord(Record r) {
        r.setId(RecordManager.instance.getUniqueId(RecordType.ANNIVERSARY));
        Record old = RecordManager.instance.insertAnniversaryRecord(r);
        if (old != null) {
            cancelAlarm(old);
            removeRecordFromDatabase(old);
        }
        registerAlarm(r);
        writeRecordToDatabase(r);
    }

    /**
     * 从后台服务删除一个闹钟记录。
     * <p>
     * 该记录将被取消定时触发。
     *
     * @param id 需要删除的记录的 id
     */
    public void removeAlarmRecord(int id) {
        Record old = RecordManager.instance.removeAlarmRecord(id);
        if (old != null) {
            cancelAlarm(old);
            removeRecordFromDatabase(old);
        }
    }

    /**
     * 从后台服务删除一个定时器记录。
     * <p>
     * 该记录将被取消定时触发。
     *
     * @param id 需要删除的记录的 id
     */
    public void removeTimerRecord(int id) {
        Record old = RecordManager.instance.removeTimerRecord(id);
        if (old != null) {
            cancelAlarm(old);
            removeRecordFromDatabase(old);
        }
    }

    /**
     * 从后台服务删除一个纪念日记录。
     * <p>
     * 该记录将被取消定时触发。
     *
     * @param id 需要删除的记录的 id
     */
    public void removeAnniversaryRecord(int id) {
        Record old = RecordManager.instance.removeAnniversaryRecord(id);
        if (old != null) {
            cancelAlarm(old);
            removeRecordFromDatabase(old);
        }
    }

    void wakeUpByAlarm(int id) {
        Record record = RecordManager.instance.getRecordById(id);
        registerAlarm(record);
    }

    private void cancelAlarm(Record record) {
        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(this.context, record.getId(), new Intent(ACTION_WAKEUP), 0));

        Log.d("AlarmManager", "Canceled Alarm " + record.getId());
    }

    private void registerAlarm(Record record) {
        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);

        if (record != null && record.isActive()) {

            if (record.getNextTriggerTime().compareTo(Calendar.getInstance()) < 0) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_CLASSNAME, record.activityToHandleThisAlarm().getName());
            bundle.putInt(EXTRA_ALARMID, record.getId());

            Intent intent = new Intent(ACTION_WAKEUP);
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, record.getId(), intent, 0);

            alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, record.getNextTriggerTime().getTimeInMillis(), pendingIntent);

            Log.d("AlarmManager", "Registered Alarm " + record.getId() + " at " + record.getNextTriggerTime().getTimeInMillis());
        }
    }

    private void initReadFromDatabase() {
        SQLiteDatabase database = new DatabaseHelper(context).getReadableDatabase();

        Record record = null;
        String statement = "select * from " + DatabaseHelper.RECORD_TABLE_NAME + " order by " + DatabaseHelper.RECORD_ID_NAME;
        Cursor cursor = database.rawQuery(statement, null);
        while (cursor.moveToNext()) {
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.RECORD_CONTENT_NAME));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                record = (Record) objectInputStream.readObject();
                insertRecordWithId(record);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        database.close();

        if (record != null) {
            RecordManager.instance.setCurrentId(record.getId());
        }
    }

    private void writeRecordToDatabase(Record record) {
        SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.RECORD_ID_NAME, record.getId());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(record);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = byteArrayOutputStream.toByteArray();
        contentValues.put(DatabaseHelper.RECORD_CONTENT_NAME, bytes);

        database.insert(DatabaseHelper.RECORD_TABLE_NAME, DatabaseHelper.RECORD_ID_NAME, contentValues);

        database.close();
    }

    private void removeRecordFromDatabase(Record record) {
        SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();
        String statement = "DELETE FROM " + DatabaseHelper.RECORD_TABLE_NAME +
                " WHERE " + DatabaseHelper.RECORD_ID_NAME + " = " + Integer.toString(record.getId());

        database.execSQL(statement);
        database.close();
    }

    void clearDatabase() {
        SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();
        database.delete(DatabaseHelper.RECORD_TABLE_NAME, null, null);
        database.close();
    }

    /**
     * 取得指定 id 对应的记录。
     *
     * @param id 记录的 id
     * @return id 对应的记录
     */
    public Record getRecordById(int id) {
        return RecordManager.instance.getRecordById(id);
    }
}
