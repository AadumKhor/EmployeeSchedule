package com.ninam.employeemanagement.data.openHelpers;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class EmployeePositionOpenHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "employeePos.db";
    public EmployeePositionOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
}
