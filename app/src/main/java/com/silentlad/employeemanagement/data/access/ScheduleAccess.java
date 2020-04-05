package com.silentlad.employeemanagement.data.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.openHelpers.ScheduleOpenHelper;
import com.silentlad.employeemanagement.data.contracts.ScheduleContract.*;

import java.io.IOException;
import java.util.Random;

public class ScheduleAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    static ScheduleAccess instance;

    private ScheduleAccess(Context context) {
        this.openHelper = new ScheduleOpenHelper(context);
    }

    public static ScheduleAccess getInstance(Context context) {
        if (instance == null) {
            instance = new ScheduleAccess(context);
        }
        return instance;
    }

    public void openDb() {
        this.database = openHelper.getReadableDatabase();
    }

    public void closeDb() {
        this.database.close();
    }

    public Cursor getDataForPosition(String posId){
        this.database = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ScheduleEntry.TABLE_NAME + " WHERE posId=?";

        return database.rawQuery(query, new String[]{posId});
    }

    public Cursor getData() {
        this.database = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ScheduleEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public Result insertSchedule(String sId,String posId, String startTime, String endTime) {
        this.database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ScheduleEntry.COLUMN_ID, sId);
        cv.put(ScheduleEntry.COLUMN_POSITION_ID, posId);
        cv.put(ScheduleEntry.COLUMN_START_TIME, startTime);
        cv.put(ScheduleEntry.COLUMN_END_TIME, endTime);

        try {
            database.insert(ScheduleEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Schedule added successfully");
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString()));
        }

    }
}
