package com.assignment.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yradex on 2016/11/28.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    static final String RECORD_TABLE_NAME = "record_table";
    static final String RECORD_ID_NAME = "record_id";
    static final String RECORD_CONTENT_NAME = "content";
    private static final String DATABASE_NAME = "record_database";


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String RECORD_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + RECORD_TABLE_NAME + " (" +
                        RECORD_ID_NAME + " INT PRIMARY KEY, " +
                        RECORD_CONTENT_NAME + " BLOB);";
        db.execSQL(RECORD_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
