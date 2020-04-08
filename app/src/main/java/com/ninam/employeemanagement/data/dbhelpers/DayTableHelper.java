package com.ninam.employeemanagement.data.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.ninam.employeemanagement.data.contracts.DayTableContract.*;
import com.ninam.employeemanagement.data.contracts.EmployeeContract;

public class DayTableHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "daytable.db";
    private static int DB_VERSION = 1;

    private static final String CREATE_TABLE = " CREATE TABLE " +DayTableEntry.TABLE_NAME + " (" +
            DayTableEntry.COLUMN_ID + " TEXT PRIMARY KEY, "+
            DayTableEntry.COLUMN_EMP_ID + " TEXT NOT NULL, "+
            DayTableEntry.COLUMN_MON + " INTEGER NOT NULL, "+
            DayTableEntry.COLUMN_TUE + " INTEGER NOT NULL, "+
            DayTableEntry.COLUMN_WED + " INTEGER NOT NULL, "+
            DayTableEntry.COLUMN_THU + " INTEGER NOT NULL, "+
            DayTableEntry.COLUMN_FRI + " INTEGER NOT NULL, "+
            DayTableEntry.COLUMN_SAT + " INTEGER NOT NULL, "+
            DayTableEntry.COLUMN_SUN + " INTEGER NOT NULL,"+
            " FOREIGN KEY ("+DayTableEntry.COLUMN_EMP_ID+") REFERENCES "+ EmployeeContract.EmployeeEntry.TABLE_NAME+
            " ("+DayTableEntry.COLUMN_EMP_ID+"));";

    public DayTableHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
