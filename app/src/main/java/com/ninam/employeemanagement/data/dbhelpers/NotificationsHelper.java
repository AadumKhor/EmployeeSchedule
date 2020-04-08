package com.ninam.employeemanagement.data.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ninam.employeemanagement.data.contracts.NotificationContract.*;
import com.ninam.employeemanagement.data.Result;

import java.io.IOException;

public class NotificationsHelper extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DB_NAME = "notifications.db";

    private static final String CREATE_TABLE = "CREATE TABLE " + NotificationEntry.TABLE_NAME + " (" +
            NotificationEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
            NotificationEntry.COLUMN_EMP_ID + " TEXT NOT NULL, " +
            NotificationEntry.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";

    public NotificationsHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotificationEntry.TABLE_NAME);
        onCreate(db);
    }

    public Result insertData(String empId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NotificationEntry.COLUMN_ID, java.util.UUID.randomUUID().toString().replace("-", ""));
        cv.put(NotificationEntry.COLUMN_EMP_ID, empId);

        try {
            db.insert(NotificationEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Notif inserted successfully");
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString()));
        }
    }

    public Result.Success checkIfExists(String empId, String timeStamp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Result.Success result = null;
        String query = "SELECT * FROM " + NotificationEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String empValueInTable = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_EMP_ID));
                String timeStampValueInTable = cursor.getString(cursor.getColumnIndex(NotificationEntry.COLUMN_TIMESTAMP));

                if (empId.equals(empValueInTable) && timeStamp.equals(timeStampValueInTable)) {
                    result = new Result.Success<>("Value exists");
                    break;
                }
            }
        }
        cursor.close();
        return result;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + NotificationEntry.TABLE_NAME + " ORDER BY timestamp ASC";

        return db.rawQuery(query, null);
    }
}
