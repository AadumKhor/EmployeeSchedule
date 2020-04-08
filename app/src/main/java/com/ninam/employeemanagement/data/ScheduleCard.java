package com.ninam.employeemanagement.data;

public class ScheduleCard {
    private String dayOfWeek;
    private String startTime;
    private String endTime;

    public ScheduleCard(String dayOfWeek, String startTime, String endTime){
        this.dayOfWeek = dayOfWeek;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
