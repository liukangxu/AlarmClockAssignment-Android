package com.assignment.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        int recordId = intent.getIntExtra(AlarmManager.EXTRA_ALARMID, 0);

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("alarm id: " + Integer.toString(recordId));

        Log.d("TestActivity", "triggered alarm: " + Integer.toString(recordId));

        AlarmManager alarmManager = new AlarmManager(this);
        alarmManager.removeAlarmRecord(recordId);
    }
}
