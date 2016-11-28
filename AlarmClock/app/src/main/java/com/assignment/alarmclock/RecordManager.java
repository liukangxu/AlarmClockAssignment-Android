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

    int getUniqueId(RecordType type) {
        int sub = type.getValue() - this.id % RecordType.EOF.getValue();
        if (sub <= 0) {
            sub += RecordType.EOF.getValue();
        }
        this.id += sub;
        return this.id;
    }

    /**
     * Returns a copy of the original AlarmRecordList.
     *
     * @return A copy of the original AlarmRecordList.
     */
    List<Record> getAlarmRecordList() {
        return new ArrayList<>(this.alarmRecordList.values());
    }

    /**
     * Returns a copy of the original CountDownRecordList.
     *
     * @return A copy of the original CountDownRecordList.
     */
    List<Record> getTimerRecordList() {
        return new ArrayList<>(this.timerRecordList.values());
    }

    /**
     * Returns a copy of the original AnniversaryRecordQueue.
     *
     * @return A copy of the original AnniversaryRecordQueue.
     */
    List<Record> getAnniversaryRecordList() {
        return new ArrayList<>(this.anniversaryRecordList.values());
    }

    Record insertAlarmRecord(Record r) {
        return alarmRecordList.put(r.getId(), r);
    }

    Record insertTimerRecord(Record r) {
        return timerRecordList.put(r.getId(), r);
    }

    Record insertAnniversaryRecord(Record r) {
        return anniversaryRecordList.put(r.getId(), r);
    }

    Record removeAlarmRecord(int id) {
        return alarmRecordList.remove(id);
    }

    Record removeTimerRecord(int id) {
        return timerRecordList.remove(id);
    }

    Record removeAnniversaryRecord(int id) {
        return anniversaryRecordList.remove(id);
    }

    Record getRecordById(int id) {
        int typeIdentifier = id % RecordType.EOF.getValue();

        if (typeIdentifier == RecordType.ALARM.getValue()) {
            return alarmRecordList.get(id);
        }
        if (typeIdentifier == RecordType.TIMER.getValue()) {
            return timerRecordList.get(id);
        }
        if (typeIdentifier == RecordType.ANNIVERSARY.getValue()) {
            return anniversaryRecordList.get(id);
        }
        return null;
    }

}

enum RecordType {
    ALARM(0), TIMER(1), ANNIVERSARY(2), EOF(3);

    private Integer value;

    RecordType(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }


}