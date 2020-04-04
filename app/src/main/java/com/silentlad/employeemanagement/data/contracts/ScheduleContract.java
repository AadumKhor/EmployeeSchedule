package com.silentlad.employeemanagement.data.contracts;

public class ScheduleContract {

    public ScheduleContract(){}

    public static class ScheduleEntry{
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_ID = "sId";
        public static final String COLUMN_POSITION_ID = "posId";
        public static final String COLUMN_START_TIME = "startTime";
        public static final String COLUMN_END_TIME = "endTime";
    }
}
