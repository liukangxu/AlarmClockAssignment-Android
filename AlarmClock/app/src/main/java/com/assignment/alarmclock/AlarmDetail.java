package com.assignment.alarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.erz.timepicker_library.TimePicker;

import static com.assignment.alarmclock.RecordType.ALARM;

public class AlarmDetail extends AppCompatActivity {
    AlarmManager alarmManager;
    boolean isNewAlarm;
    int alarmRecordId;
    AlarmRecord alarmRecord;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        alarmManager = new AlarmManager(this);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isNewAlarm", true)) {
            isNewAlarm = true;
            Log.d("Detail:", "New");
        } else {
            isNewAlarm = false;
            alarmRecordId = intent.getIntExtra("alarmRecordId", 0);
            AlarmRecord alarmRecord = (AlarmRecord) alarmManager.getRecordById(alarmRecordId);
            timePicker.setTime(alarmRecord.getTime().getTime());
            if (alarmRecord.getRepeatDays()[0]) {
                ((ToggleButton)findViewById(R.id.sundayToggleButton)).setChecked(true);
            }
            if (alarmRecord.getRepeatDays()[1]) {
                ((ToggleButton)findViewById(R.id.mondayToggleButton)).setChecked(true);
            }
            if (alarmRecord.getRepeatDays()[2]) {
                ((ToggleButton)findViewById(R.id.tuesdayToggleButton)).setChecked(true);
            }
            if (alarmRecord.getRepeatDays()[3]) {
                ((ToggleButton)findViewById(R.id.wednesdayToggleButton)).setChecked(true);
            }
            if (alarmRecord.getRepeatDays()[4]) {
                ((ToggleButton)findViewById(R.id.thursdayToggleButton)).setChecked(true);
            }
            if (alarmRecord.getRepeatDays()[5]) {
                ((ToggleButton)findViewById(R.id.fridayToggleButton)).setChecked(true);
            }
            if (alarmRecord.getRepeatDays()[6]) {
                ((ToggleButton)findViewById(R.id.saturdayToggleButton)).setChecked(true);
            }
            Log.d("Detail:", "Edit");
        }
    }

    public void saveAlarmDetail(View view) {
        AlarmManager alarmManager = new AlarmManager(this);

        boolean isRepeat = false;
        boolean[] repeatDays = new boolean[7];

        if (((ToggleButton)findViewById(R.id.sundayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[0] = true;
        }
        if (((ToggleButton)findViewById(R.id.mondayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[1] = true;
        }
        if (((ToggleButton)findViewById(R.id.tuesdayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[2] = true;
        }
        if (((ToggleButton)findViewById(R.id.wednesdayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[3] = true;
        }
        if (((ToggleButton)findViewById(R.id.thursdayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[4] = true;
        }
        if (((ToggleButton)findViewById(R.id.fridayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[5] = true;
        }
        if (((ToggleButton)findViewById(R.id.saturdayToggleButton)).isChecked()) {
            isRepeat = true;
            repeatDays[6] = true;
        }

        AlarmRecord record = new AlarmRecord(timePicker.getTime(), isRepeat, repeatDays, true);
        alarmManager.insertRecord(ALARM, record);
        if (!isNewAlarm) {
            alarmManager.removeRecord(alarmRecordId);
        }
        this.finish();
    }
}
