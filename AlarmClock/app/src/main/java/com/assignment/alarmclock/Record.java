package com.assignment.alarmclock;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by yradex on 2016/11/23.
 */

public interface Record extends Serializable {
    Calendar getNextTriggerTime();

    boolean isActive();
}

class RecordComparator implements Comparator<Record> {

    @Override
    public int compare(Record o1, Record o2) {
        if (o1.isActive() != o2.isActive()) {
            return o1.isActive() ? -1 : 1;
        }
        return o1.getNextTriggerTime().compareTo(o2.getNextTriggerTime());
    }
}
