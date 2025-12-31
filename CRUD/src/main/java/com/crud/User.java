package com.crud;

public class User {
    private int id;
    private String email;
    private String password;
    private String role;      // "teacher" ya "student" store karega
    private int studentId;    // Agar student hai to uski ID, teacher hai to 0/Null

    // Default Constructor (Zaroori hota hai)
    public User() {
    }

    // Constructor for saving new user
    public User(String email, String password, String role, int studentId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
    }

    // Constructor for reading from database (ID ke sath)
    public User(int id, String email, String password, String role, int studentId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
}