package com.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    // Database connection details
    private String jdbcURL = "jdbc:mysql://localhost:3306/my_school?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = ""; // Apna password yahan dalein
    private Connection jdbcConnection;

    // 1. Database se Connect karne ka method
    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    // 2. Connection close karne ka method
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    // 3. MAIN METHOD: Naya User Register karna
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password, role, student_id) VALUES (?, ?, ?, ?)";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getRole());

        // Logic: Agar studentId 0 hai (mtlb Teacher hai), to NULL set karo
        if (user.getStudentId() == 0) {
            statement.setNull(4, java.sql.Types.INTEGER);
        } else {
            statement.setInt(4, user.getStudentId());
        }

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }







    //// Method to check login credentials
    public User checkLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, password);

        ResultSet rs = statement.executeQuery();

        User user = null;

        if (rs.next()) {
            // Agar user mila, to uska data nikal kar object banao
            user = new User();
            user.setId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));

            // Student ID bhi nikalna zaroori hai (Student login ke liye)
            user.setStudentId(rs.getInt("student_id"));
        }

        rs.close();
        statement.close();
        disconnect();

        return user; // Agar galat password tha to 'null' return hoga
    }
}