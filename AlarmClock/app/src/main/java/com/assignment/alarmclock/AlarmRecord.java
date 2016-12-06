package com.assignment.alarmclock;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

class AlarmRecord implements Record {
    private int id;
    private Calendar calendar = Calendar.getInstance();
    private boolean isRepeat;
    private boolean[] repeatDays = new boolean[7];
    private boolean isActive;

    AlarmRecord(Date time, boolean isRepeat, boolean[] repeatDays, boolean isActive) {
        this.calendar.setTime(time);
        this.calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        this.calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        this.calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        while (this.calendar.before(Calendar.getInstance())) {
            this.calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d("Calendar:", this.calendar.toString());
        this.isRepeat = isRepeat;
        this.repeatDays = repeatDays;
        this.isActive = isActive;
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
        Log.d("CurrentTime:", "" + Calendar.getInstance().getTime().toString());
        if (!isRepeat) {
            Log.d("NextTriggerTime:", calendar.getTime().toString());
            if (calendar.before(Calendar.getInstance())) {
                isActive = false;
            }
            return calendar;
        } else {
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            Calendar nextTriggerTime = Calendar.getInstance();
            nextTriggerTime.setTime(calendar.getTime());

            Calendar current_date = Calendar.getInstance();
            int current_day = current_date.get(Calendar.DAY_OF_WEEK) - 1;

            for (int i = current_day; i < 7; i++) {
                if (repeatDays[i] && nextTriggerTime.get(Calendar.DAY_OF_WEEK) == current_date.get(Calendar.DAY_OF_WEEK) && current_date.before(nextTriggerTime)) {
                    Log.d("NextTriggerTime:", calendar.getTime().toString());
                    return calendar;
                }
                if (repeatDays[i] && current_date.after(nextTriggerTime)) {
                    while ((int) nextTriggerTime.get(Calendar.DAY_OF_WEEK) - 1 != i) {
                        nextTriggerTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    calendar.setTime(nextTriggerTime.getTime());
                    Log.d("NextTriggerTime:", calendar.getTime().toString());
                    return calendar;
                }
                current_date.add(Calendar.DAY_OF_MONTH, 1);
            }

            for (int i = 0; i < current_day; i++) {
                if (repeatDays[i]) {
                    while ((int) nextTriggerTime.get(Calendar.DAY_OF_WEEK) - 1 != i) {
                        nextTriggerTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    calendar.setTime(nextTriggerTime.getTime());
                    Log.d("NextTriggerTime:", calendar.getTime().toString());
                    return calendar;
                }
                current_date.add(Calendar.DAY_OF_MONTH, 1);
            }

            nextTriggerTime.set(Calendar.YEAR, current_date.get(Calendar.YEAR));
            nextTriggerTime.set(Calendar.MONTH, current_date.get(Calendar.MONTH));
            nextTriggerTime.set(Calendar.DAY_OF_MONTH, current_date.get(Calendar.DAY_OF_MONTH));
            calendar.setTime(nextTriggerTime.getTime());
            Log.d("NextTriggerTime:", calendar.getTime().toString());
            return calendar;
        }
    }

    Calendar getTime() {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(calendar.getTime());
        tempCalendar.add(Calendar.HOUR_OF_DAY, 8);
        return calendar;
    }

    boolean[] getRepeatDays() {
        return repeatDays;
    }

    boolean isRepeat() {
        return isRepeat;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public RecordHandler getHandler() {
        return null;
    }

    @Override
    public Class activityToHandleThisAlarm() {
        return AlarmAlertActivity.class;
    }
}
