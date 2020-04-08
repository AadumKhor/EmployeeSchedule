package com.ninam.employeemanagement.data.contracts;

public class NotificationContract {
    public NotificationContract(){}

    public static class NotificationEntry{
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_EMP_ID = "empId";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
