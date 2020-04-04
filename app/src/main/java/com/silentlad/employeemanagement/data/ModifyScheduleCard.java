package com.silentlad.employeemanagement.data;

public class ModifyScheduleCard {
    private String sId;
    private String daysOfTheWeek;
    private String startTime;
    private String endTime;
    private String position;
    private String name;

    public ModifyScheduleCard(String sID, String daysOfTheWeek, String startTime, String endTime, String position, String name) {
        this.sId = sID;
        this.daysOfTheWeek = daysOfTheWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.position = position;
        this.name = name;
    }

    public String getSId(){
        return sId;
    }

    public String getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
