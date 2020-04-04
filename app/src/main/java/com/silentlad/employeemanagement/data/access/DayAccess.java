package com.silentlad.employeemanagement.data.access;


import android.content.ContentValues;
import android.content.Context;
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

    public void closeDb(){
        this.db.close();
    }

    public Result insertDayData(String dId, String empId, boolean sunday, boolean monday, boolean tuesday, boolean wednesday,
                                boolean thursday, boolean friday, boolean saturday) {
        this.db = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DayTableEntry.COLUMN_ID, dId);
        cv.put(DayTableEntry.COLUMN_EMP_ID, empId);
        cv.put(DayTableEntry.COLUMN_SUN, sunday);
        cv.put(DayTableEntry.COLUMN_MON, monday);
        cv.put(DayTableEntry.COLUMN_TUE, tuesday);
        cv.put(DayTableEntry.COLUMN_WED, wednesday);
        cv.put(DayTableEntry.COLUMN_THU, thursday);
        cv.put(DayTableEntry.COLUMN_FRI, friday);
        cv.put(DayTableEntry.COLUMN_SAT, saturday);

        try{
            db.insert(DayTableEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Days schedule added successfully");
        }catch (Exception e){
            return new Result.Error(new IOException(e.toString()));
        }

    }
}
