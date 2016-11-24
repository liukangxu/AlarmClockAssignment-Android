package com.assignment.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yradex on 2016/11/23.
 */

enum RecordManager {

    instance;

    private final RecordComparator comparator = new RecordComparator();
    private final LinkedList<Record> alarmRecordList = new LinkedList<>();
    private final LinkedList<Record> timerRecordList = new LinkedList<>();
    private final LinkedList<Record> anniversaryRecordList = new LinkedList<>();
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
        return new ArrayList<>(this.alarmRecordList);
    }

    /**
     * Returns a copy of the original CountDownRecordList.
     *
     * @return A copy of the original CountDownRecordList.
     */
    List<Record> getTimerRecordList() {
        return new ArrayList<>(this.timerRecordList);
    }

    /**
     * Returns a copy of the original AnniversaryRecordQueue.
     *
     * @return A copy of the original AnniversaryRecordQueue.
     */
    List<Record> getAnniversaryRecordList() {
        return new ArrayList<>(this.anniversaryRecordList);
    }

    void insertAlarmRecord(Record r) {
        this.alarmRecordList.add(r);
        Collections.sort(this.alarmRecordList, this.comparator);
    }

    void insertTimerRecord(Record r) {
        this.timerRecordList.add(r);
        Collections.sort(this.timerRecordList, this.comparator);
    }

    void insertAnniversaryRecord(Record r) {
        this.anniversaryRecordList.add(r);
        Collections.sort(this.anniversaryRecordList, this.comparator);
    }

    boolean removeAlarmRecord(Record r) {
        return this.alarmRecordList.remove(r);
    }

    boolean removeTimerRecord(Record r) {
        return this.timerRecordList.remove(r);
    }

    boolean removeAnniversaryRecord(Record r) {
        return this.anniversaryRecordList.remove(r);
    }

    Record getRecordById(int id) {
        final int nid = id;
        Record record = new Record() {
            @Override
            public int getId() {
                return nid;
            }

            @Override
            public Calendar getNextTriggerTime() {
                return null;
            }

            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public Class activityToHandleThisAlarm() {
                return null;
            }
        };

        int typeIdentifier = id % RecordType.EOF.getValue();
        if (typeIdentifier == RecordType.ALARM.getValue()) {
            int index = Collections.binarySearch(this.alarmRecordList, record, this.comparator);
            if (index < 0) {
                return null;
            }
            return this.alarmRecordList.get(index);
        }
        if (typeIdentifier == RecordType.TIMER.getValue()) {
            int index = Collections.binarySearch(this.timerRecordList, record, this.comparator);
            if (index < 0) {
                return null;
            }
            return this.timerRecordList.get(index);
        }
        if (typeIdentifier == RecordType.ANNIVERSARY.getValue()) {
            int index = Collections.binarySearch(this.anniversaryRecordList, record, this.comparator);
            if (index < 0) {
                return null;
            }
            return this.anniversaryRecordList.get(index);
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