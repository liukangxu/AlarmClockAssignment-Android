package com.assignment.alarmclock;

import java.util.Calendar;

/**
 * Created by jinha on 2016/12/4.
 */

public class AnniversaryRecord implements Record {
    private int id;
    private String statement;
    private Calendar calendar = Calendar.getInstance();
    private boolean ifActive;
    private int delay;

    AnniversaryRecord(int _year, int _month, int _day, String _statement, boolean _ifActive, int _delay) {

        this.statement = _statement;
        this.ifActive = _ifActive;
        this.delay = _delay;
        this.calendar.set(_year, _month, _day, 8, 0, 0);
        this.calendar.add(Calendar.DATE, -delay);
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
        return this.ifActive;
    }

    @Override
    public RecordHandler getHandler() {
        return null;
    }

    @Override
    public Class activityToHandleThisAlarm() {
        return null;
    }

    public String getStatement() {
        return String.valueOf(this.statement);
    }
}
