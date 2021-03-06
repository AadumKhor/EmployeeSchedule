package com.ninam.employeemanagement.data.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ninam.employeemanagement.data.Result;
import com.ninam.employeemanagement.data.contracts.EmployeeContract;
import com.ninam.employeemanagement.data.openHelpers.EmployeeOpenHelper;

import java.io.IOException;

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

    public void openDb() {
        this.db = openHelper.getWritableDatabase();
    }

    public void closeDb() {
        this.db.close();
    }

    public Cursor getData() {
        this.db = openHelper.getReadableDatabase();

        String query = "SELECT * FROM " + EmployeeContract.EmployeeEntry.TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public String getName(String empId) {
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT firstName,lastName FROM " + EmployeeContract.EmployeeEntry.TABLE_NAME + " WHERE id=?";

        Cursor cursor = db.rawQuery(query, new String[]{empId});
        String fullName = "";
        while (cursor.moveToNext()) {
            fullName = cursor.getString(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_F_NAME)) +
                    " " + cursor.getString(cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_L_NAME));
        }
        cursor.close();
        return fullName;
    }

    public Result insertNewEmployee(String id, String firstName, String lastName) {
        this.db = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("firstName", firstName);
        cv.put("lastName", lastName);
        try {
            db.insert("employee", null, cv);
            return new Result.Success<>("Employee added");
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString()));
        }
    }

}
