package com.silentlad.employeemanagement.data.openHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class ScheduleOpenHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "schedule.db";
    private static final int DB_VERSION = 1;

    public ScheduleOpenHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }
}
