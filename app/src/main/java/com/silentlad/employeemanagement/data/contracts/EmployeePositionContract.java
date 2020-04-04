package com.silentlad.employeemanagement.data.contracts;

import android.provider.BaseColumns;

public class EmployeePositionContract {

    public EmployeePositionContract(){}

    public static class EmployeePositionEntry implements BaseColumns{
        public static final String TABLE_NAME = "employeePos";
        public static final String COLUMN_ID = "posId";
        public static final String COLUMN_POSITION = "empPos";
        public static final String COLUMN_EMP_ID = "empId";
    }
}
