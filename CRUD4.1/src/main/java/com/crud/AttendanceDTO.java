package com.crud;

public class AttendanceDTO {
    private String rollNo;
    private String name;
    private String status;

    // Getters and Setters generate kar lein
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}