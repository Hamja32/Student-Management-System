package com.crud;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
//constructor
	public StudentDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {

		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
//database connection method
	protected void connect() throws SQLException {
		if(jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
		}
	}
	protected void disconnect() throws SQLException {
		if(jdbcConnection != null && jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	//show one student result.................
	public List<Result> getStudentResult(int id) throws SQLException{
		List<Result> list = new ArrayList<>();
	    
	    // Query same rahegi (JOIN wali)
	    String query = "SELECT s.roll_no, s.name, s.course, r.subject, r.marks_obtained, r.total_marks " +
	                   "FROM students s " +
	                   "INNER JOIN results r ON s.id = r.student_id " +
	                   "WHERE s.id = ?";
	    
	    connect();
	    PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
	    pstmt.setInt(1, id);
	    
	    ResultSet rs = pstmt.executeQuery();

	    // Loop chalayenge taki saare subjects aa jayein
	    while (rs.next()) {
	        Result result = new Result();
	        
	        // Student Info
	        result.setStudentName(rs.getString("name"));
	        result.setRollNo(rs.getString("roll_no"));
	        result.setCourse(rs.getString("course"));
	        
	        // Subject Marks
	        result.setSubject(rs.getString("subject"));
	        result.setMarks_obtained(rs.getInt("marks_obtained"));
	        result.setTotal_marks(rs.getInt("total_marks"));
	        
	        // List me add karein
	        list.add(result);
	    }
	    
	    rs.close();
	    pstmt.close();
	    return list;
	}
	
	
//insert student method
	public boolean insertStudent(Student student) throws SQLException{
		String query = "INSERT INTO students(roll_no,name,dob,course,address) VALUES(?,?,?,?,?)";
		connect();
		PreparedStatement statement = jdbcConnection.prepareStatement(query);
		statement.setInt(1,student.getRoll_no());
		statement.setString(2, student.getName());
		statement.setString(3, student.getDob());
		statement.setString(4, student.getCourse());
		statement.setString(5, student.getAddress());
		boolean rowInserted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();


		return rowInserted;

	}
//show student method
	public List<Student> showAllStudents() throws SQLException{
		List<Student> listStudent = new ArrayList<>();
		String query = "SELECT * FROM students";
		connect();
		Statement stmt =  jdbcConnection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			int id = rs.getInt("id");
			int roll_no = rs.getInt("roll_no");
			String dob = rs.getString("dob");
			String name = rs.getString("name");
			String course = rs.getString("course");
			String address = rs.getString("address");

			Student student = new Student(id,roll_no,name,dob,address,course);
			listStudent.add(student);
		}
		rs.close();
		stmt.close();
		disconnect();
		return listStudent;
	}
//delete student
	public boolean deleteStudent(Student student)throws SQLException {
		String query = "DELETE FROM students WHERE id = ?";
		connect();
		PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
		pstmt.setInt(1, student.getId());
		boolean rowDeleted = pstmt.executeUpdate() > 0;
		pstmt.close();
		disconnect();

		return rowDeleted;
	}
//update student
	public boolean updateStudent(Student student)throws SQLException {
		String query = "UPDATE students SET roll_no = ?, dob = ?,name = ?,course = ?,address = ? WHERE id = ?";
		connect();
		PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
		pstmt.setInt(1, student.getRoll_no());
		pstmt.setString(2,student.getDob());
		pstmt.setString(3, student.getName());
		pstmt.setString(4, student.getCourse());
		pstmt.setString(5, student.getAddress());
		pstmt.setInt(6, student.getId());

		boolean rowUpdated = pstmt.executeUpdate() > 0;
		pstmt.close();
		disconnect();
		return rowUpdated;
	}
//get one Student

	public Student getStudent(int id) throws SQLException{
		Student student = null;
		String query = "SELECT * FROM students WHERE id = ?";
		connect();

		PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
		pstmt.setInt(1, id);
		ResultSet rs =  pstmt.executeQuery();
		if(rs.next()) {
			int roll_no = rs.getInt("roll_no");
			String dob = rs.getString("dob");
			String name = rs.getString("name");
			String address = rs.getString("address");
			String course = rs.getString("course");
			student = new Student(id,roll_no,name,dob,address,course);
		}
		rs.close();
		pstmt.close();
		return student;
	}
	// 1. Total Students ginnne ke liye
	public int getTotalStudents() throws SQLException {
	    String sql = "SELECT COUNT(*) FROM students";
	    connect();
	    PreparedStatement pstmt = jdbcConnection.prepareStatement(sql);
	    ResultSet rs = pstmt.executeQuery();
	    
	    int count = 0;
	    if(rs.next()) {
	        count = rs.getInt(1);
	    }
	    
	    rs.close();
	    pstmt.close();
	    disconnect(); // Agar alag se method hai to call karein, ya connection close karein
	    return count;
	}

	// 2. Total Login Users ginnne ke liye (Optional: UserDAO me bhi rakh sakte hain par yahan easy hai)
	public int getTotalUsers() throws SQLException {
	    String sql = "SELECT COUNT(*) FROM users"; // Users table count
	    connect();
	    PreparedStatement pstmt = jdbcConnection.prepareStatement(sql);
	    ResultSet rs = pstmt.executeQuery();
	    
	    int count = 0;
	    if(rs.next()) {
	        count = rs.getInt(1);
	    }
	    
	    rs.close();
	    pstmt.close();
	    return count;
	}
}


