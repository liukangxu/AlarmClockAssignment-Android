package com.assignment.alarmclock;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yradex on 2016/11/23.
 */

enum RecordManager {

    instance;

    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, Record> alarmRecordList = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, Record> timerRecordList = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, Record> anniversaryRecordList = new HashMap<>();
    private int id = 1;

    public void setCurrentId(int id) {
        this.id = id;
    }

    int getUniqueId(RecordType type) {
        int sub = type.getValue() - this.id % RecordType.NULL.getValue();
        if (sub <= 0) {
            sub += RecordType.NULL.getValue();
        }
        this.id += sub;
        return this.id;
    }

    RecordType getRecordTypeById(int id) {
        int typeIdentifier = id % RecordType.NULL.getValue();

        if (typeIdentifier == RecordType.ALARM.getValue()) {
            return RecordType.ALARM;
        }
        if (typeIdentifier == RecordType.TIMER.getValue()) {
            return RecordType.TIMER;
        }
        if (typeIdentifier == RecordType.ANNIVERSARY.getValue()) {
            return RecordType.ANNIVERSARY;
        }
        return RecordType.NULL;
    }

    /**
     * Returns a copy of the original Record List.
     *
     * @return A copy of the original Record List.
     */
    List<Record> getRecordList(RecordType type) {
        switch (type) {
            case ALARM:
                return new ArrayList<>(this.alarmRecordList.values());
            case TIMER:
                return new ArrayList<>(this.timerRecordList.values());
            case ANNIVERSARY:
                return new ArrayList<>(this.anniversaryRecordList.values());
            case NULL:
                return null;
        }
        return null;
    }

    Record insertRecord(Record record) {
        RecordType type = getRecordTypeById(record.getId());
        switch (type) {
            case ALARM:
                return alarmRecordList.put(record.getId(), record);
            case TIMER:
                return timerRecordList.put(record.getId(), record);
            case ANNIVERSARY:
                return anniversaryRecordList.put(record.getId(), record);
            case NULL:
                throw new IllegalArgumentException();
        }
        return null;
    }

    Record removeRecord(int id) {
        RecordType type = getRecordTypeById(id);
        switch (type) {
            case ALARM:
                return alarmRecordList.remove(id);
            case TIMER:
                return timerRecordList.remove(id);
            case ANNIVERSARY:
                return anniversaryRecordList.remove(id);
            case NULL:
                break;
        }
        return null;
    }

    Record getRecordById(int id) {
        switch (getRecordTypeById(id)) {
            case ALARM:
                return alarmRecordList.get(id);
            case TIMER:
                return timerRecordList.get(id);
            case ANNIVERSARY:
                return anniversaryRecordList.get(id);
            case NULL:
                return null;
        }
        return null;
    }

    void clear() {
        alarmRecordList.clear();
        timerRecordList.clear();
        anniversaryRecordList.clear();
        setCurrentId(0);
    }

}
