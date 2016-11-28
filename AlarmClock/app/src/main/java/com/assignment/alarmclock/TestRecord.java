package com.assignment.alarmclock;

import java.util.Calendar;

/**
 * Created by yradex on 2016/11/24.
 */


class TestRecord implements Record {

    private int id;
    private Calendar calendar = Calendar.getInstance();

    TestRecord() {
        this.calendar.add(Calendar.SECOND, 5);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Calendar getNextTriggerTime() {
        return this.calendar;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Class activityToHandleThisAlarm() {
        return TestActivity.class;
    }
}

