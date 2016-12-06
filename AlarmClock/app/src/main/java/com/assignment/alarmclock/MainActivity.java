package com.assignment.alarmclock;

import android.content.Intent;
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

    public void runTimer(View view) {
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }

    public void runAlarm(View view) {
        Intent intent = new Intent(this, AlarmMainActivity.class);
        startActivity(intent);
    }

    public void runAnni(View view) {
        Intent intent = new Intent(this, AnniversayActivity.class);
        startActivity(intent);
    }

    public void clearDatabase(View view) {
        AlarmManager alarmManager = new AlarmManager(this);
        alarmManager.clearDatabase();
    }
}
