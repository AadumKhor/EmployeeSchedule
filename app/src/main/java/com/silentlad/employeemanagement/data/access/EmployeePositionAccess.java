package com.silentlad.employeemanagement.data.access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.silentlad.employeemanagement.data.openHelpers.EmployeePositionOpenHelper;

public class EmployeePositionAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static EmployeePositionAccess instance;

    private EmployeePositionAccess(Context context) {
        this.openHelper = new EmployeePositionOpenHelper(context);
    }

    public static EmployeePositionAccess getInstance(Context context) {
        if (instance == null) {
            instance = new EmployeePositionAccess(context);
        }
        return instance;
    }

    public void openDb() {
        this.db = openHelper.getWritableDatabase();
    }
}
