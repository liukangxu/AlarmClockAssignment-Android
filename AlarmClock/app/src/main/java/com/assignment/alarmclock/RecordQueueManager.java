package com.assignment.alarmclock;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by yradex on 2016/11/23.
 */

public enum RecordQueueManager {

    instance;

    private final RecordComparator comparator = new RecordComparator();
    private final Queue<Record> alarmRecordQueue = new PriorityQueue<>(1, this.comparator);
    private final Queue<Record> timerRecordQueue = new PriorityQueue<>(1, this.comparator);
    private final Queue<Record> anniversaryRecordQueue = new PriorityQueue<>(1, this.comparator);

    /**
     * Returns a copy of the original AlarmRecordList.
     *
     * @return A copy of the original AlarmRecordList.
     */
    List<Record> getAlarmRecordList() {
        return new ArrayList<>(this.alarmRecordQueue);
    }

    /**
     * Returns a copy of the original CountDownRecordList.
     *
     * @return A copy of the original CountDownRecordList.
     */
    List<Record> getTimerRecordList() {
        return new ArrayList<>(this.timerRecordQueue);
    }

    /**
     * Returns a copy of the original AnniversaryRecordQueue.
     *
     * @return A copy of the original AnniversaryRecordQueue.
     */
    List<Record> getAnniversaryRecordList() {
        return new ArrayList<>(this.anniversaryRecordQueue);
    }

    void insertAlarmRecord(Record r) {
        this.alarmRecordQueue.add(r);
    }

    void insertTimerRecord(Record r) {
        this.timerRecordQueue.add(r);
    }

    void insertAnniversaryRecord(Record r) {
        this.anniversaryRecordQueue.add(r);
    }

    boolean removeAlarmRecord(Record r) {
        return this.alarmRecordQueue.remove(r);
    }

    boolean removeTimerRecord(Record r) {
        return this.timerRecordQueue.remove(r);
    }

    boolean removeAnniversaryRecord(Record r) {
        return this.anniversaryRecordQueue.remove(r);
    }

}
