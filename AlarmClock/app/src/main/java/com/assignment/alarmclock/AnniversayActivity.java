package com.assignment.alarmclock;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AnniversayActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AnniversaryRecord dateToShow;
    private List<Record> list;
    View.OnClickListener SelectItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Iterator<Record> ir = list.iterator();
            while (ir.hasNext()) {
                AnniversaryRecord ar = (AnniversaryRecord) ir.next();

                if (ar.getId() == v.getId()) {
                    //Toast.makeText(AnniversayActivity.this, "HERE", Toast.LENGTH_SHORT).show();
                    dateToShow = ar;
                    setShow();
                    break;
                }
            }
        }
    };
    private Calendar reserveCal;
    private String reserveStr;
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            reserveStr = null;
            switch (menuItem.getItemId()) {
                case R.id.action_add:
                    addDate(toolbar);
                    break;
                case R.id.action_alter:
                    reserveCal = null;
                    if (dateToShow != null) {
                        reserveCal = dateToShow.getNextTriggerTime();
                        reserveStr = dateToShow.getStatement();
                        AlarmManager alarmManager = new AlarmManager(AnniversayActivity.this);
                        alarmManager.removeRecord(dateToShow.getId());
                    }
                    addDate(toolbar);
                    break;
                case R.id.action_delete:
                    if (dateToShow != null) {
                        AlarmManager alarmManager = new AlarmManager(AnniversayActivity.this);
                        alarmManager.removeRecord(dateToShow.getId());
                    }
                    print();
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversay);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Anniversary");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        print();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mune_anniversay, menu);
        return true;
    }

    public void addDate(View view) {
        Semaphore SS;
        final int[] date = new int[3];
        final String[] statement = new String[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(AnniversayActivity.this);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请输入标题");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View v = LayoutInflater.from(AnniversayActivity.this).inflate(R.layout.dialog_anniversary, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(v);

        final EditText username = (EditText) v.findViewById(R.id.username);
        if (reserveCal != null) {
            username.setText(reserveStr);
            reserveStr = null;
        }

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                statement[0] = username.getText().toString().trim();

                AlarmManager alarmManager = new AlarmManager(AnniversayActivity.this);
                AnniversaryRecord record = new AnniversaryRecord(date[0], date[1], date[2], statement[0], true, 0);
                alarmManager.insertRecord(RecordType.ANNIVERSARY, record);

                print();
            }
        });

        if (reserveCal == null) {
            reserveCal = Calendar.getInstance();
        }
        DatePickerDialog pd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //选择完时间后会调用该回调函数
            public void onDateSet(DatePicker view, int year, int month, int day) {
                date[0] = year;
                date[1] = month;
                date[2] = day;

                builder.show();


            }
        }, reserveCal.get(Calendar.YEAR), reserveCal.get(Calendar.MONTH), reserveCal.get(Calendar.DATE));
        pd.show();
        reserveCal = null;

    }

    private void print() {
        LinearLayout ly;
        ly = (LinearLayout) findViewById(R.id.ly);
        ly.removeAllViews();
        View divid = new View(ly.getContext());
        RelativeLayout.LayoutParams s;
        s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 2);
        divid.setLayoutParams(s);
        divid.setBackgroundColor(Color.BLACK);
        dateToShow = null;
        AlarmManager alarmManager = new AlarmManager(AnniversayActivity.this);

        class cmp implements Comparator<Record> {
            @Override
            public int compare(Record o1, Record o2) {
                Calendar c1 = o1.getNextTriggerTime();
                Calendar c2 = o2.getNextTriggerTime();
                return c1.compareTo(c2);
            }
        }
        list = alarmManager.getRecordList(RecordType.ANNIVERSARY, new cmp());
        Iterator<Record> ir = list.iterator();

        ly.addView(divid);
        while (ir.hasNext()) {
            AnniversaryRecord ar = (AnniversaryRecord) ir.next();
            if (dateToShow == null)
                dateToShow = ar;
            final StringBuffer time = new StringBuffer();
            time.append(ar.getNextTriggerTime().get(Calendar.YEAR) + "-" + (ar.getNextTriggerTime().get(Calendar.MONTH) + 1) + "-" + ar.getNextTriggerTime().get(Calendar.DATE));

            ly = (LinearLayout) findViewById(R.id.ly);
            LinearLayout localLy = new LinearLayout(AnniversayActivity.this);
            TextView b1 = new TextView(ly.getContext());
            TextView b2 = new TextView(ly.getContext());
            TextView b3 = new TextView(ly.getContext());

            TextPaint pt;

            b1.setText("  - " + time);
            s = new RelativeLayout.LayoutParams(280, 130);
            s.addRule(RelativeLayout.CENTER_VERTICAL);
            b1.setLayoutParams(s);
            pt = b1.getPaint();
            pt.setFakeBoldText(true);


            s = new RelativeLayout.LayoutParams(550, 130);
            s.addRule(RelativeLayout.CENTER_VERTICAL);
            b2.setLayoutParams(s);
            b2.setText(ar.getStatement());
            b2.setTextColor(Color.BLACK);
            b2.setTextSize(20);


            Calendar localCl = ar.getNextTriggerTime();
            Calendar nowTime = Calendar.getInstance();
            nowTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), 0, 0, 0);
            long interdays = (localCl.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (1000 * 60 * 60 * 24) + 1;
            b3.setText(interdays + "");
            pt = b3.getPaint();
            pt.setFakeBoldText(true);
            s = new RelativeLayout.LayoutParams(300, 130);
            s.addRule(RelativeLayout.CENTER_VERTICAL);
            b3.setTextSize(25);
            b3.setLayoutParams(s);
            b3.setTextColor(Color.rgb(0, 28, 88));
            localLy.setBackgroundColor(Color.WHITE);
            localLy.addView(b1);
            localLy.addView(b2);
            localLy.addView(b3);
            localLy.setId(ar.getId());
            localLy.setOnClickListener(SelectItem);
            ly.addView(localLy);

            divid = new View(ly.getContext());
            s = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 2);
            divid.setLayoutParams(s);
            divid.setBackgroundColor(Color.BLACK);
            ly.addView(divid);
        }

        setShow();

    }

    void setShow() {
        if (dateToShow != null) {
            TextView content = (TextView) findViewById(R.id.showStatement);
            content.setText(dateToShow.getStatement());
            content = (TextView) findViewById(R.id.showDate);
            Calendar localCl = dateToShow.getNextTriggerTime();
            content.setText(localCl.get(Calendar.YEAR) + "-" + (localCl.get(Calendar.MONTH) + 1) + "-" + localCl.get(Calendar.DATE));
            Calendar nowTime = Calendar.getInstance();
            nowTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), 0, 0, 0);
            long interdays = (localCl.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (1000 * 60 * 60 * 24) + 1;

            content = (TextView) findViewById(R.id.showCount);
            content.setText(interdays + "");
        } else {
            TextView content = (TextView) findViewById(R.id.showStatement);
            content.setText("请添加纪念日");

            content = (TextView) findViewById(R.id.showDate);
            content.setText("请添加日期");

            content = (TextView) findViewById(R.id.showCount);
            content.setText("0");
        }
    }


}
