package com.ninam.employeemanagement.data.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ninam.employeemanagement.data.contracts.ActualTimingContract.*;
import com.ninam.employeemanagement.data.contracts.EmployeeContract.*;
import com.ninam.employeemanagement.data.Result;

import java.io.IOException;

public class ActualTimingHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "actualTime.db";

    private static final String CREATE_TABLE = "CREATE TABLE " + ActualTimingEntry.TABLE_NAME + "(" +
            ActualTimingEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
            ActualTimingEntry.COLUMN_EMP_ID + " TEXT NOT NULL, " +
            ActualTimingEntry.COLUMN_TIME_IN + " TEXT NOT NULL, " +
            ActualTimingEntry.COLUMN_TIME_OUT + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + ActualTimingEntry.COLUMN_EMP_ID + ") REFERENCES " + EmployeeEntry.TABLE_NAME +
            "(" + ActualTimingEntry.COLUMN_EMP_ID + "));";


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

    public Result insertData(String empId, String timeIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.println(Log.DEBUG, "insert", "before cv");
        cv.put(ActualTimingEntry.COLUMN_ID, java.util.UUID.randomUUID().toString().replace("-", ""));
        cv.put(ActualTimingEntry.COLUMN_EMP_ID, empId);
        cv.put(ActualTimingEntry.COLUMN_TIME_IN, timeIn);
        cv.put(ActualTimingEntry.COLUMN_TIME_OUT, "Not entered yet");
        Log.println(Log.DEBUG, "insert", "after");
        try {
            db.insert(ActualTimingEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Data entered to actual time db successfully");
        } catch (Exception e) {
            Log.println(Log.DEBUG, "insert", e.toString());
            return new Result.Error(new IOException(e.toString()));

        }
    }

    public Result updateData(String empId, String outTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ActualTimingEntry.COLUMN_TIME_OUT, outTime);

        try {
            db.update(ActualTimingEntry.TABLE_NAME, cv, "empId=?", new String[]{empId});
            return new Result.Success<>("Out time successfully entered");
        } catch (Exception e) {
            return new Result.Error(new IOException("Data could not be updated"));
        }
    }
}
