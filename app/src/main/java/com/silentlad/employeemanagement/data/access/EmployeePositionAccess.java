package com.silentlad.employeemanagement.data.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.contracts.EmployeePositionContract;
import com.silentlad.employeemanagement.data.openHelpers.EmployeePositionOpenHelper;

import java.io.IOException;

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

    public void closeDb() {
        this.db.close();
    }

    public Cursor getData() {
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT * FROM " + EmployeePositionContract.EmployeePositionEntry.TABLE_NAME + " WHERE empId=?";

        return db.rawQuery(query, new String[]{"abcdef"});
    }

    public Result insertData(String posId, String empId, String position) {
        this.db = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EmployeePositionContract.EmployeePositionEntry.COLUMN_ID, posId);
        cv.put(EmployeePositionContract.EmployeePositionEntry.COLUMN_EMP_ID, empId);
        cv.put(EmployeePositionContract.EmployeePositionEntry.COLUMN_POSITION, position);

        try {
            db.insert(EmployeePositionContract.EmployeePositionEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Position Added Successfully");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error adding position"));
        }

    }
}
