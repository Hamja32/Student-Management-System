package com.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    
    // Database connection code (wahi purana wala copy kar lein)
    private String jdbcURL = "jdbc:mysql://localhost:3306/my_school"; // Apne DB ka naam check karein
    private String jdbcUsername = "root";
    private String jdbcPassword = ""; // Apna password dalein
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

    // --- MARK ATTENDANCE METHOD ---
    public void markAttendance(int studentId, String date, String status) {
        // Query: Agar pehle se entry hai to insert mat karo (filhal simple insert rakhte hain)
        String sql = "INSERT INTO attendance (student_id, at_date, status) VALUES (?, ?, ?)";
        
        try {
            connect();
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setString(2, date);
            ps.setString(3, status);
            
            ps.executeUpdate();
            
            ps.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // Method: Ek specific date ki attendance lana
    public List<AttendanceDTO> getAttendanceByDate(String date) {
        List<AttendanceDTO> list = new ArrayList<>();
        
        // JOIN Query: Students table se Naam lo, Attendance table se Status lo
        String sql = "SELECT s.roll_no, s.name, a.status " +
                     "FROM students s " +
                     "JOIN attendance a ON s.id = a.student_id " +
                     "WHERE a.at_date = ?";
        
        try {
            connect();
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Hum ek temporary object (DTO) use karenge data store karne ke liye
                AttendanceDTO dto = new AttendanceDTO();
                dto.setRollNo(rs.getString("roll_no"));
                dto.setName(rs.getString("name"));
                dto.setStatus(rs.getString("status"));
                list.add(dto);
            }
            
            rs.close();
            ps.close();
            disconnect();
        } catch (Exception e) { e.printStackTrace(); }
        
        return list;
    }
    
    
 // 1. Check method: Kya attendance pehle se hai?
    public boolean isAttendanceMarked(int studentId, String date) {
        boolean isMarked = false;
        String sql = "SELECT * FROM attendance WHERE student_id = ? AND at_date = ?";
        
        try {
            connect();
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setString(2, date);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                isMarked = true; // Haan, data hai
            }
            
            rs.close();
            ps.close();
            disconnect();
        } catch (Exception e) { e.printStackTrace(); }
        
        return isMarked;
    }

    // 2. Update method: Galti sudharne ke liye
    public void updateAttendance(int studentId, String date, String newStatus) {
        String sql = "UPDATE attendance SET status = ? WHERE student_id = ? AND at_date = ?";
        
        try {
            connect();
            PreparedStatement ps = jdbcConnection.prepareStatement(sql);
            ps.setString(1, newStatus);
            ps.setInt(2, studentId);
            ps.setString(3, date);
            
            ps.executeUpdate(); // Update chalao
            
            ps.close();
            disconnect();
        } catch (Exception e) { e.printStackTrace(); }
    }
}