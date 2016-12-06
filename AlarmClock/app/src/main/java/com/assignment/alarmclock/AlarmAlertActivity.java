package com.assignment.alarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class AlarmAlertActivity extends AppCompatActivity {
    private Vibrator vib;
    private MediaPlayer mp;
    private int alarmId;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", Calendar.getInstance().getTime().toString());

        vib.cancel();
        mp.stop();
        mp.release();
        AlarmManager alarmManager = new AlarmManager(this);
        AlarmRecord alarmRecord = (AlarmRecord) alarmManager.getRecordById(alarmId);
        if (!alarmRecord.isRepeat()) {
            alarmRecord.setActive(false);
            alarmManager.updateRecord(alarmRecord);
        }
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_alert);
        Intent intent = getIntent();
        alarmId = intent.getIntExtra(AlarmManager.EXTRA_ALARMID, 0);

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);

        vib = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(new long[]{1000, 500, 1000, 500}, 1);

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
        try {
            mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.setLooping(true);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDismissClick(View view) {
        Log.d("onDismiss", Calendar.getInstance().getTime().toString());
        this.finish();
    }
}
