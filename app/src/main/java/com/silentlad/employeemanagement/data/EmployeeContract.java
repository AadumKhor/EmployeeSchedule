package com.silentlad.employeemanagement.data;

import android.provider.BaseColumns;

public class EmployeeContract {
    public EmployeeContract(){}

    public static class EmployeeEntry implements BaseColumns{
        public static final String TABLE_NAME = "employee";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_F_NAME = "firstName";
        public static final String COLUMN_L_NAME = "lastName";
    }
}
