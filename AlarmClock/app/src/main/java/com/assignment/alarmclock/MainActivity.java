package com.assignment.alarmclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmManager.globalInitialize(this);
    }

    public void runService(View view) {
        AlarmManager alarmManager = new AlarmManager(this);
        TestRecord record = new TestRecord();
        alarmManager.insertAlarmRecord(record);
    }

    public void clearDatabase(View view) {
        AlarmManager alarmManager = new AlarmManager(this);
        alarmManager.clearDatabase();
    }
}
