package com.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {
    
    // Connection wahi purana wala (Copy paste kar lein ya extend karein)
    private String jdbcURL = "jdbc:mysql://localhost:3306/my_school";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";
    private Connection jdbcConnection;

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }
    
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) { jdbcConnection.close(); }
    }

    // 1. Total Students Ginna
    public int getTotalStudents() {
        int count = 0;
        try {
            connect();
            Statement stmt = jdbcConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM students");
            if(rs.next()) count = rs.getInt(1);
            disconnect();
        } catch(Exception e) { e.printStackTrace(); }
        return count;
    }

    // 2. Pass/Fail Count krna (Thoda Tricky Logic)
    // Hum maan rahe hain agar total marks > 33% hai to pass
    public int getPassCount() {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT student_id) FROM results " +
                     "GROUP BY student_id " +
                     "HAVING (SUM(marks_obtained) / SUM(total_marks)) * 100 >= 33";
        // Note: Ye query check karti hai ki overall percentage 33 se zyada hai ya nahi
        
        try {
            connect();
            Statement stmt = jdbcConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // Resultset ka size nikalna padega
            while(rs.next()) { count++; } // Jitne rows aaye utne bache pass hain
            disconnect();
        } catch(Exception e) { e.printStackTrace(); }
        return count;
    }
    
    //topper
    public List<TopStudent> getTop3Students() {
        List<TopStudent> list = new ArrayList<>();
        
        // 🔥 Advanced Query
        String sql = "SELECT s.name, s.roll_no, " +
                     "(SUM(r.marks_obtained) * 100.0 / SUM(r.total_marks)) as percent " +
                     "FROM students s " +
                     "JOIN results r ON s.id = r.student_id " +
                     "GROUP BY s.id " +
                     "ORDER BY percent DESC " +
                     "LIMIT 3";
                     
        try {
            connect();
            Statement stmt = jdbcConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                String name = rs.getString("name");
                String roll = rs.getString("roll_no");
                double per = rs.getDouble("percent");
                
                list.add(new TopStudent(name, roll, per));
            }
            disconnect();
        } catch(Exception e) { e.printStackTrace(); }
        
        return list;
    }
}