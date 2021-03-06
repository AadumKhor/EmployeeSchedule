package com.ninam.employeemanagement.data.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ninam.employeemanagement.data.Result;
import com.ninam.employeemanagement.data.contracts.EmployeePositionContract;
import com.ninam.employeemanagement.data.openHelpers.EmployeePositionOpenHelper;

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

        return db.rawQuery(query, new String[]{});
    }

    public Cursor getEmpDetails(String empId){
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT posId,empPos FROM " + EmployeePositionContract.EmployeePositionEntry.TABLE_NAME + " WHERE empId=?";

        return db.rawQuery(query, new String[]{empId});
    }

    public String getEmpId(String posId) {
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT empId FROM " + EmployeePositionContract.EmployeePositionEntry.TABLE_NAME + " WHERE posId=?";
        Log.println(Log.DEBUG, "position id", query);
        Cursor cursor = db.rawQuery(query, new String[]{posId});
        Log.println(Log.DEBUG, "position id", "2");
        String empId = "";
        while (cursor.moveToNext()) {
            Log.println(Log.DEBUG, "position id", "while....");
            empId = cursor.getString(cursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_EMP_ID));
            Log.println(Log.DEBUG, "position id", empId);
        }
//        Log.println(Log.DEBUG, "position id", empId);
        cursor.close();
        return empId;
    }

    public String getPosition(String posId) {
        this.db = openHelper.getReadableDatabase();
        String query = "SELECT empPos FROM " + EmployeePositionContract.EmployeePositionEntry.TABLE_NAME + " WHERE posId=?";

        Cursor cursor = db.rawQuery(query, new String[]{posId});

        String position = "";
        while (cursor.moveToNext()) {
            position = cursor.getString(cursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_POSITION));
        }
        cursor.close();
        return position;
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
