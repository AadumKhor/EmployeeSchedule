package com.ninam.employeemanagement.data.openHelpers;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class EmployeeOpenHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "employee.db";
    private static int DB_VERSION = 1;

    public EmployeeOpenHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }
}
