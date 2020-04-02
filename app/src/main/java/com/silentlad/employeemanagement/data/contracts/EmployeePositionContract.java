package com.silentlad.employeemanagement.data.contracts;

import android.provider.BaseColumns;

public class EmployeePositionContract {

    public EmployeePositionContract(){}

    public static class EmployeePositionEntry implements BaseColumns{
        public static final String TABLE_NAME = "employeePosition";
        public static final String COLUMN_ID = "positionId";
        public static final String COLUMN_POSITION = "employeePosition";
        public static final String COLUMN_EMP_ID = "empId";
    }
}
