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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 程序记录的管理器。
 * <p>
 * 自动持久化所有记录，自动安排记录的触发。
 * <p>
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
     * 取得所有记录的列表。应某些人要求，列表按照记录插入的顺序排序。
     * <p>
     * 在这个列表中进行修改不会影响到后台服务的工作。
     *
     * @param type 需要查询的记录类型
     * @return 所有指定类型记录的列表
     */
    public List<Record> getRecordList(RecordType type) {
        if (type == RecordType.NULL) {
            throw new IllegalArgumentException();
        }
        List<Record> list = RecordManager.instance.getRecordList(type);
        assert list != null;
        Collections.sort(list, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o1.getId() - o2.getId();
            }
        });
        return list;
    }

    /**
     * 取得所有记录的列表。列表按照指定的比较器进行排序。
     * <p>
     * 在这个列表中进行修改不会影响到后台服务的工作。
     *
     * @param type 需要查询的记录类型
     * @return 所有指定类型记录的列表
     */
    public List<Record> getRecordList(RecordType type, Comparator<Record> recordComparator) {
        if (type == RecordType.NULL) {
            throw new IllegalArgumentException();
        }
        List<Record> list = RecordManager.instance.getRecordList(type);
        assert list != null;
        Collections.sort(list, recordComparator);
        return list;
    }

    private void insertRecordFromDatabase(Record r) {
        RecordManager.instance.insertRecord(r);
        registerAlarm(r);
    }

    /**
     * 向后台服务插入一个记录。记录的 id 将会被后台服务自动设定。
     * <p>
     * 该记录将被安排定时触发。
     *
     * @param type   记录的类型
     * @param record 需要插入的记录
     */
    public void insertRecord(RecordType type, Record record) {
        if (type == RecordType.NULL) {
            throw new IllegalArgumentException();
        }
        record.setId(RecordManager.instance.getUniqueId(type));
        Record old = RecordManager.instance.insertRecord(record);
        if (old != null) {
            cancelAlarm(old.getId());
            removeRecordFromDatabase(old);
        }
        registerAlarm(record);
        writeRecordToDatabase(record);
    }

    /**
     * 从后台服务删除一个记录。
     * <p>
     * 该记录将被取消定时触发。
     *
     * @param id 需要删除的记录的 id
     */
    public void removeRecord(int id) {
        Record old = RecordManager.instance.removeRecord(id);
        if (old != null) {
            cancelAlarm(old.getId());
            removeRecordFromDatabase(old);
        }
    }

    void wakeUpByAlarm(int id) {
        Record record = RecordManager.instance.getRecordById(id);
        registerAlarm(record);
    }

    private void cancelAlarm(int id) {
        android.app.AlarmManager
                alarmManager = (android.app.AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(this.context, id, new Intent(ACTION_WAKEUP), 0));

        Log.d("AlarmManager", "Canceled Alarm " + id);
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
                insertRecordFromDatabase(record);
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

    /**
     * 清空程序的数据库。
     * <p>
     * 程序的所有记录将会清空。仅供测试使用。
     */
    public void clearDatabase() {
        RecordManager.instance.clear();
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
