package com.assignment.alarmclock;

import java.io.Serializable;
import java.util.Calendar;

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
