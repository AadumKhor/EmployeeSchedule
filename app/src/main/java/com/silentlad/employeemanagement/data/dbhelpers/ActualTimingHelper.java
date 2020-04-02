package com.silentlad.employeemanagement.data.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.silentlad.employeemanagement.data.ActualTimingContract.*;
import com.silentlad.employeemanagement.data.EmployeeContract.*;

public class ActualTimingHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "actualTime.db";

    private static final String CREATE_TABLE = "CREATE TABLE " + ActualTimingEntry.TABLE_NAME + "("+
        ActualTimingEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
            ActualTimingEntry.COLUMN_EMP_ID + " TEXT NOT NULL, " +
            ActualTimingEntry.COLUMN_TIME_IN + " TEXT NOT NULL, "+
            ActualTimingEntry.COLUMN_TIME_OUT + " TEXT NOT NULL, "+
            " FOREIGN KEY ("+ActualTimingEntry.COLUMN_EMP_ID+") REFERENCES "+EmployeeEntry.TABLE_NAME+
            "("+EmployeeEntry.COLUMN_ID+"));";


    public ActualTimingHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ActualTimingEntry.TABLE_NAME);
        onCreate(db);
    }
}
