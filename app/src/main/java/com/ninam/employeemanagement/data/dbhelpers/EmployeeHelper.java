package com.ninam.employeemanagement.data.dbhelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ninam.employeemanagement.data.contracts.EmployeeContract.*;

import java.util.Random;

public class EmployeeHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "employee.db";

    private static final String CREATE_TABLE = "CREATE TABLE " + EmployeeEntry.TABLE_NAME + " (" +
            EmployeeEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
            EmployeeEntry.COLUMN_F_NAME + " TEXT NOT NULL, " +
            EmployeeEntry.COLUMN_L_NAME + " TEXT NOT NULL" + ");";

    public EmployeeHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

//    private void createDb(){
//        InputStream stream =
//    }

    String random() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int maxLength = 6;
        char tempChar;
        for (int i = 0; i < maxLength; i++) {
            tempChar = (char) (random.nextInt(25) + 97);
            sb.append(tempChar);
        }
        return sb.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeEntry.TABLE_NAME);
        onCreate(db);
    }
}
