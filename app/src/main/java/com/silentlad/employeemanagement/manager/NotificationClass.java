package com.silentlad.employeemanagement.manager;

public class NotificationClass {
    private String notifId;
    private String empId;
    private String fullName;

    private String date;
    private String timeStamp;

    public NotificationClass(String notifId, String empId, String fullName, String date, String timeStamp) {
        this.notifId = notifId;
        this.empId = empId;
        this.fullName = fullName;
        this.date = date;
        this.timeStamp = timeStamp;
    }


    public String getFullName() {
        return fullName;
    }

    public String getNotifId() {
        return notifId;
    }

    public String getEmpId() {
        return empId;
    }

    public String getDate() {
        return date;
    }
}
