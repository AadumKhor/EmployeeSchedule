package com.silentlad.employeemanagement.data;

public class ActualTimingContract {

    public ActualTimingContract(){}

    public static class ActualTimingEntry{
        public static final String TABLE_NAME = "actualTiming";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TIME_IN = "inTime";
        public static final String COLUMN_TIME_OUT = "outTime";
        public static final String COLUMN_EMP_ID = "empId";
    }
}
