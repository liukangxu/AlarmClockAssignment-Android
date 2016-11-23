package com.assignment.alarmclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void runService(View view) {
        AlarmManager.getInstance(this).invokeAfterFiveSeconds(TestActivity.class.getName());
    }
}
