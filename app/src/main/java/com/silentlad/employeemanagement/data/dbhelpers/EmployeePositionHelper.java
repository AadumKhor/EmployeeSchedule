package com.silentlad.employeemanagement.data.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.silentlad.employeemanagement.data.contracts.EmployeeContract.*;
import com.silentlad.employeemanagement.data.contracts.EmployeePositionContract.*;

public class EmployeePositionHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "employeePosition";

    private static final String CREATE_TABLE = "CREATE TABLE "+ EmployeePositionEntry.TABLE_NAME+ " ("+
            EmployeePositionEntry.COLUMN_ID + " TEXT PRIMARY KEY, "+
            EmployeePositionEntry.COLUMN_EMP_ID + " TEXT NOT NULL, "+
            EmployeePositionEntry.COLUMN_POSITION + " TEXT NOT NULL, "+
            " FOREIGN KEY ("+EmployeePositionEntry.COLUMN_EMP_ID+") REFERENCES "+EmployeeEntry.TABLE_NAME+
            " ("+EmployeePositionEntry.COLUMN_EMP_ID+"));";


    public EmployeePositionHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeePositionEntry.TABLE_NAME);
        onCreate(db);
    }
}
