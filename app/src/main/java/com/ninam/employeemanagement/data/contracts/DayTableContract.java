package com.ninam.employeemanagement.data.contracts;

public class DayTableContract {

    public DayTableContract(){}

    public static class DayTableEntry{
        public static final String TABLE_NAME = "days";
        public static final String COLUMN_ID = "dId";
        public static final String COLUMN_EMP_ID = "empId";
        public static final String COLUMN_SID = "sId";
        public static final String COLUMN_SUN = "sunday";
        public static final String COLUMN_MON = "monday";
        public static final String COLUMN_TUE = "tuesday";
        public static final String COLUMN_WED = "wednesday";
        public static final String COLUMN_THU = "thursday";
        public static final String COLUMN_FRI = "friday";
        public static final String COLUMN_SAT = "saturday";
    }
}
