package com.silentlad.employeemanagement.data.openHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DayOpenHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "day_table.db";
    private static final int DB_VERSION = 1;

    public DayOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
