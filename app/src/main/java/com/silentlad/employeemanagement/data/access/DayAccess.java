package com.silentlad.employeemanagement.data.access;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.openHelpers.DayOpenHelper;
import com.silentlad.employeemanagement.data.contracts.DayTableContract.*;

import java.io.IOException;
import java.util.Random;

public class DayAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DayAccess instance;

    private DayAccess(Context context) {
        this.openHelper = new DayOpenHelper(context);
    }

    public static DayAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DayAccess(context);
        }
        return instance;
    }

    public void openDb() {
        this.db = openHelper.getReadableDatabase();
    }

    public void closeDb() {
        this.db.close();
    }

    public String[] getDays(String empId) {
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DayTableEntry.TABLE_NAME + " WHERE empId=?";
        Cursor cursor = db.rawQuery(query, new String[]{empId});

        String[] result = new String[7];

        while (cursor.moveToNext()) {
            result[0] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_SUN));
            result[1] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_MON));
            result[2] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_TUE));
            result[3] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_WED));
            result[4] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_THU));
            result[5] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_FRI));
            result[6] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_SAT));
        }

        cursor.close();
        return result;
    }

    public String[] getDaysForSchedule(String empId, String sId){
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DayTableEntry.TABLE_NAME + " WHERE empId=? AND sId=?";
        Cursor cursor = db.rawQuery(query, new String[]{empId, sId});

        String[] result = new String[7];

        while (cursor.moveToNext()) {
            result[0] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_SUN));
            result[1] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_MON));
            result[2] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_TUE));
            result[3] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_WED));
            result[4] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_THU));
            result[5] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_FRI));
            result[6] = cursor.getString(cursor.getColumnIndex(DayTableEntry.COLUMN_SAT));
        }

        cursor.close();
        return result;
    }

//    public Result updateDaysSchedule(String empId){}

    public Result insertDayData(String dId, String empId, String sId,boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
                                boolean thursday, boolean friday, boolean saturday) {
        this.db = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DayTableEntry.COLUMN_ID, dId);
        cv.put(DayTableEntry.COLUMN_EMP_ID, empId);
        cv.put(DayTableEntry.COLUMN_S_ID, sId);
        cv.put(DayTableEntry.COLUMN_SUN, sunday);
        cv.put(DayTableEntry.COLUMN_MON, monday);
        cv.put(DayTableEntry.COLUMN_TUE, tuesday);
        cv.put(DayTableEntry.COLUMN_WED, wednesday);
        cv.put(DayTableEntry.COLUMN_THU, thursday);
        cv.put(DayTableEntry.COLUMN_FRI, friday);
        cv.put(DayTableEntry.COLUMN_SAT, saturday);

        try {
            db.insert(DayTableEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Days schedule added successfully");
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString()));
        }

    }
}
