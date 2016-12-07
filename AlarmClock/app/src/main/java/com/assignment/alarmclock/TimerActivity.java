package com.assignment.alarmclock;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {

    DecimalFormat formatter = new DecimalFormat("00");
    CountDownTimer countDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        NumberPicker secondPicker = (NumberPicker) findViewById(R.id.secondPicker);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);

        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);

        AlarmManager alarmManager = new AlarmManager(this);
        if (!alarmManager.getRecordList(RecordType.TIMER).isEmpty()) {
            TimerRecord timerRecord = (TimerRecord) alarmManager.getRecordList(RecordType.TIMER).get(0);
            long millisUntilFinished = timerRecord.getNextTriggerTime().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            if (millisUntilFinished > 0) {
                int mills = (int) (millisUntilFinished % 1000);
                int seconds = (int) (millisUntilFinished / 1000 % 60);
                int minutes = (int) (millisUntilFinished / 1000 / 60 % 60);
                int hours = (int) (millisUntilFinished / 1000 / 3600);
                startTimer(hours, minutes, seconds, mills, false);
            }
        }

        switchUI();
    }

    public void startTimer(int hours, int minutes, int seconds, int mills, boolean isNew) {
        assert countDownTimer == null;

        int timeToStart = ((hours * 60 + minutes) * 60 + seconds) * 1000 + mills;

        if (timeToStart == 0) {
            return;
        }

        TimerRecord timerRecord = new TimerRecord(hours, minutes, seconds);

        countDownTimer = new CountDownTimer(timeToStart, 200) {

            public void onTick(long millisUntilFinished) {
                long secondsUntilFinished = millisUntilFinished / 1000 + 1;
                long hours = secondsUntilFinished / 3600;
                long minutes = secondsUntilFinished / 60 % 60;
                long seconds = secondsUntilFinished % 60;

                TextView textView = (TextView) findViewById(R.id.timerTextview);
                textView.setText(formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds));
            }

            public void onFinish() {
                TextView textView = (TextView) findViewById(R.id.timerTextview);
                textView.setText(formatter.format(0) + ":" + formatter.format(0) + ":" + formatter.format(0));
                Log.d("Timer", "finished");
//                textView.setTextColor(Color.RED);
            }
        }.start();

        TextView textView = (TextView) findViewById(R.id.timerTextview);
        textView.setText(formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds));
        textView.setTextColor(Color.GRAY);

        AlarmManager alarmManager = new AlarmManager(this);
        if (isNew) {
            alarmManager.insertRecord(RecordType.TIMER, timerRecord);
        }
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        AlarmManager alarmManager = new AlarmManager(this);
        if (!alarmManager.getRecordList(RecordType.TIMER).isEmpty()) {
            alarmManager.removeRecord(alarmManager.getRecordList(RecordType.TIMER).get(0).getId());
        }
    }

    public void switchTimer(View view) {

        if (countDownTimer == null) {
            NumberPicker secondPicker = (NumberPicker) findViewById(R.id.secondPicker);
            NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
            NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);

            int hours = hourPicker.getValue();
            int minutes = minutePicker.getValue();
            int seconds = secondPicker.getValue();
            startTimer(hours, minutes, seconds, 0, true);
        } else {
            stopTimer();
        }
        switchUI();
    }

    public void switchUI() {
        Button button = (Button) findViewById(R.id.timerSwitchButton);
        TextView textView = (TextView) findViewById(R.id.timerTextview);
        NumberPicker hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        NumberPicker secondPicker = (NumberPicker) findViewById(R.id.secondPicker);
        NumberPicker minutePicker = (NumberPicker) findViewById(R.id.minutePicker);

        if (countDownTimer == null) {
            secondPicker.setVisibility(View.VISIBLE);
            minutePicker.setVisibility(View.VISIBLE);
            hourPicker.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            button.setText("开始");
        } else {
            secondPicker.setVisibility(View.INVISIBLE);
            minutePicker.setVisibility(View.INVISIBLE);
            hourPicker.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
            button.setText("取消");
        }
    }
}
