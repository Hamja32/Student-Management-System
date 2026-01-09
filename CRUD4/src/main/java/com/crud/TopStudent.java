package com.crud;

public class TopStudent {
    private String name;
    private String rollNo;
    private double percentage;

    // Constructor
    public TopStudent(String name, String rollNo, double percentage) {
        this.name = name;
        this.rollNo = rollNo;
        this.percentage = percentage;
    }

    // Getters
    public String getName() { return name; }
    public String getRollNo() { return rollNo; }
    public double getPercentage() { return percentage; }
}