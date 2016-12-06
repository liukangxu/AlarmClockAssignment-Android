package com.assignment.alarmclock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.assignment.alarmclock.RecordType.ALARM;

public class AlarmMainActivity extends AppCompatActivity {
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        AlarmManager.globalInitialize(this);
        alarmManager = new AlarmManager(this);
        refreshAlarmList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        toolbar.inflateMenu(R.menu.menu_alarm_main);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.alarm_menu_add) {
                    Toast.makeText(AlarmMainActivity.this, R.string.alarm_menu_add, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AlarmMainActivity.this, AlarmDetail.class);
                    intent.putExtra("isNewAlarm", true);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void onResume() {
        super.onResume();
        refreshAlarmList();
    }

    private void refreshAlarmList() {
        ListView alarmListView = (ListView) findViewById(R.id.alarm_list);
        SimpleAdapter alarmListAdapter = new SimpleAdapter(this, getAlarmRecordList(), R.layout.alarm_list_item, new String[]{"time", "repeatDays"}, new int[]{R.id.alarm_time, R.id.alarm_days});
        alarmListView.setAdapter(alarmListAdapter);

        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlarmMainActivity.this, AlarmDetail.class);
                intent.putExtra("isNewAlarm", false);
                ListView listView = (ListView) adapterView;
                HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(i);
                intent.putExtra("alarmRecordId", (Integer) map.get("id"));
                startActivity(intent);
            }
        });

        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmMainActivity.this);
                builder.setMessage("确认删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Dialog", "confirm");
                        Log.d("DeleteId", "" + view.getTag());
                        alarmManager.removeRecord((int) view.getTag());
                        refreshAlarmList();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Dialog", "cancel");
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        alarmListView.post(new Runnable() {
            @Override
            public void run() {
                ListView alarmListView = (ListView) findViewById(R.id.alarm_list);
                for (int i = 0; i < alarmListView.getChildCount(); i++) {
                    ((Switch) alarmListView.getChildAt(i).findViewById(R.id.enable_switch)).setTag((int) ((HashMap<String, Object>) alarmListView.getItemAtPosition(i)).get("id"));
                    alarmListView.getChildAt(i).setTag((int) ((HashMap<String, Object>) alarmListView.getItemAtPosition(i)).get("id"));
                    ((Switch) alarmListView.getChildAt(i).findViewById(R.id.enable_switch)).setChecked((boolean) ((HashMap<String, Object>) alarmListView.getItemAtPosition(i)).get("isActive"));
                }
            }
        });
    }

    public void onEnableSwitchClicked(View view) {
        AlarmRecord alarmRecord = (AlarmRecord) alarmManager.getRecordById((int) view.getTag());
        boolean status = ((Switch) view).isChecked();
        if (status) {
            alarmRecord.setActive(true);
        } else {
            alarmRecord.setActive(false);
        }
        alarmManager.updateRecord(alarmRecord);
    }

    private ArrayList<HashMap<String, Object>> getAlarmRecordList() {
        ArrayList<HashMap<String, Object>> alarmRecordList = new ArrayList<>();
        List<Record> alarmRecords = alarmManager.getRecordList(ALARM);

        for (int i = 0; i < alarmRecords.size(); i++) {
            String repeatDays = "";
            AlarmRecord alarmRecord = (AlarmRecord) alarmRecords.get(i);
            if (alarmRecord.getRepeatDays()[0]) {
                repeatDays += " 周日";
            }
            if (alarmRecord.getRepeatDays()[1]) {
                repeatDays += " 周一";
            }
            if (alarmRecord.getRepeatDays()[2]) {
                repeatDays += " 周二";
            }
            if (alarmRecord.getRepeatDays()[3]) {
                repeatDays += " 周三";
            }
            if (alarmRecord.getRepeatDays()[4]) {
                repeatDays += " 周四";
            }
            if (alarmRecord.getRepeatDays()[5]) {
                repeatDays += " 周五";
            }
            if (alarmRecord.getRepeatDays()[6]) {
                repeatDays += " 周六";
            }
            if (repeatDays.length() == 0) {
                repeatDays = "不重复";
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", alarmRecord.getId());
            map.put("time", alarmRecord.getTime().get(Calendar.HOUR_OF_DAY) + ":" + alarmRecord.getTime().get(Calendar.MINUTE));
            map.put("repeatDays", repeatDays);
            map.put("isActive", alarmRecord.isActive());
            alarmRecordList.add(map);
        }

        return alarmRecordList;
    }
}
