package com.ninam.employeemanagement.data.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ninam.employeemanagement.data.Result;
import com.ninam.employeemanagement.data.openHelpers.ScheduleOpenHelper;
import com.ninam.employeemanagement.data.contracts.ScheduleContract.*;

import java.io.IOException;

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

    public Cursor getData() {
        this.database = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ScheduleEntry.TABLE_NAME;
        return database.rawQuery(query, null);
    }

    public String[] getScheduleWithScheduleId(String sId) {
        this.database = openHelper.getReadableDatabase();
        String query = "SELECT startTime,endTime FROM " + ScheduleEntry.TABLE_NAME + " WHERE sId=?";

        Cursor cursor = database.rawQuery(query, new String[]{sId});
        String[] result = new String[2];
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                result[0] = cursor.getString(cursor.getColumnIndex(ScheduleEntry.COLUMN_START_TIME));
                result[1] = cursor.getString(cursor.getColumnIndex(ScheduleEntry.COLUMN_END_TIME));
            }
        }
        cursor.close();
        return result;
    }

    public Result insertSchedule(String sId, String posId, String startTime, String endTime) {
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

    public Result updateSchedule(String sId, String startTime, String endTime) {
        this.database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ScheduleEntry.COLUMN_START_TIME, startTime);
        cv.put(ScheduleEntry.COLUMN_END_TIME, endTime);

        try {
            int i = database.update("schedule", cv, "sId=?", new String[]{sId});
            return new Result.Success<>(i);
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString()));
        }
    }

    public Result deleteEntry(String sId) {
        this.database = openHelper.getWritableDatabase();
        try {
            int i = database.delete("schedule", "sId=?", new String[]{sId});
            return new Result.Success<>(String.valueOf(i));
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString()));
        }
    }

    public String getPosId(String sId) {
        this.database = openHelper.getReadableDatabase();
        String query = "SELECT posId FROM schedule where sId=?";
        Cursor cursor = database.rawQuery(query, new String[]{sId});
        String posId = "";
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                posId = cursor.getString(cursor.getColumnIndex(ScheduleEntry.COLUMN_POSITION_ID));
            }
        }
        cursor.close();
        return posId;
    }

    public Cursor getDataForPosition(String posId) {
        this.database = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ScheduleEntry.TABLE_NAME + " WHERE posId=?";

        return database.rawQuery(query, new String[]{posId});
    }
}
