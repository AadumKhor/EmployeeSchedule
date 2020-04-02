package com.silentlad.employeemanagement.data.dbhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.silentlad.employeemanagement.data.contracts.NotificationContract.*;
import com.silentlad.employeemanagement.data.Result;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationsHelper extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DB_NAME = "notifications.db";

    private static final String CREATE_TABLE = "CREATE TABLE " + NotificationEntry.TABLE_NAME + " (" +
            NotificationEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
            NotificationEntry.COLUMN_EMP_ID + " TEXT NOT NULL, "+
            NotificationEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL" + ");";

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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Result insertData(String empId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NotificationEntry.COLUMN_ID, java.util.UUID.randomUUID().toString().replace("-", ""));
        cv.put(NotificationEntry.COLUMN_EMP_ID, empId);
        cv.put(NotificationEntry.COLUMN_TIMESTAMP, getDateTime());

        try{
            db.insert(NotificationEntry.TABLE_NAME, null, cv);
            return new Result.Success<>("Notif inserted successfully");
        }catch(Exception e){
            return new Result.Error(new IOException(e.toString()));
        }
    }

    public Result getData(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + NotificationEntry.TABLE_NAME;

        try {
            Cursor cursor = db.rawQuery(query, null);
            cursor.close();
            return new Result.Success<>("Data retrieved.");
        }catch(Exception e){
            return new Result.Error(new IOException("Error retrieving data."));
        }
    }
}
