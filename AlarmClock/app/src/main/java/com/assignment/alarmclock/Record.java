package com.assignment.alarmclock;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by yradex on 2016/11/23.
 */

interface Record extends Serializable {

    int getId();

    void setId(int id);

    Calendar getNextTriggerTime();

    boolean isActive();

    Class activityToHandleThisAlarm();
}

class RecordComparatorById implements Comparator<Record> {

    @Override
    public int compare(Record o1, Record o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            }
            return 1;
        }
        if (o2 == null) {
            return -1;
        }
        return o1.getId() - o2.getId();
    }
}
