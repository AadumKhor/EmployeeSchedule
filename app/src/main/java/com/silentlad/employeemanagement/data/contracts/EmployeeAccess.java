package com.silentlad.employeemanagement.data.contracts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.openHelpers.EmployeeOpenHelper;

public class EmployeeAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static EmployeeAccess instance;
    Cursor c = null;

    private EmployeeAccess(Context context) {
        this.openHelper = new EmployeeOpenHelper(context);
    }

    public static EmployeeAccess getInstance(Context context) {
        if (instance == null) {
            instance = new EmployeeAccess(context);
        }
        return instance;
    }

    public void openDb(){
        this.db = openHelper.getWritableDatabase();
    }

    public Cursor getData() {
        this.db = openHelper.getReadableDatabase();

        String query = "SELECT * FROM employee";
        return db.rawQuery(query, null);
    }

}
