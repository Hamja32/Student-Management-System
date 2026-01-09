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
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}

	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}

	// show one student result.................
	public List<Result> getStudentResult(int id) throws SQLException {
		List<Result> list = new ArrayList<>();

		// Query same rahegi (JOIN wali)
		String query = "SELECT s.roll_no, s.name, s.course, s.photo_name,r.subject, r.marks_obtained, r.total_marks "
				+ "FROM students s " + "INNER JOIN results r ON s.id = r.student_id " + "WHERE s.id = ?";

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
			// --- NEW: PHOTO NAME SET KAREIN ---
			// Note: Agar photo nahi hai to null aayega, jo thik hai
			result.setPhotoName(rs.getString("photo_name"));
			// List me add karein
			list.add(result);
		}

		rs.close();
		pstmt.close();
		return list;
	}
	// 1. Student ID se saare result lana
	public List<Result> getResultsByStudentId(int studentId) {
	    List<Result> list = new ArrayList<>();
	    String sql = "SELECT * FROM results WHERE student_id = ?";
	    
	    try {
	        connect();
	        PreparedStatement ps = jdbcConnection.prepareStatement(sql);
	        ps.setInt(1, studentId);
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            Result r = new Result();
	            r.setId(rs.getInt("id"));
	            r.setStudent_id(studentId);
	            r.setSubject(rs.getString("subject"));
	            r.setMarks_obtained(rs.getInt("marks_obtained"));
	            r.setTotal_marks(rs.getInt("total_marks"));
	            list.add(r);
	        }
	        rs.close();
	        ps.close();
	        disconnect();
	    } catch (Exception e) { e.printStackTrace(); }
	    return list;
	}

	// 2. Marks Update karna
	public void updateSingleResult(int id, int marks) throws SQLException {
	    String sql = "UPDATE results SET marks_obtained = ? WHERE id = ?";
	    connect();
	    PreparedStatement ps = jdbcConnection.prepareStatement(sql);
	    ps.setInt(1, marks);
	    ps.setInt(2, id);
	    ps.executeUpdate();
	    ps.close();
	    disconnect();
	}

	// 3. Insert karna (Jo aapke paas shayad pehle se ho)
	public void insertResult(Result res) throws SQLException {
	    String sql = "INSERT INTO results (student_id, subject, total_marks, marks_obtained) VALUES (?, ?, ?, ?)";
	    connect();
	    PreparedStatement ps = jdbcConnection.prepareStatement(sql);
	    ps.setInt(1, res.getStudent_id());
	    ps.setString(2, res.getSubject());
	    ps.setInt(3, res.getTotal_marks());
	    ps.setInt(4, res.getMarks_obtained());
	    ps.executeUpdate();
	    ps.close();
	    disconnect();
	}
	
	
	//result part end
	
//insert student method
	public boolean insertStudent(Student student) throws SQLException {
		String query = "INSERT INTO students(roll_no,name,dob,course,address,photo_name) VALUES(?,?,?,?,?,?)";
		connect();
		PreparedStatement statement = jdbcConnection.prepareStatement(query);
		statement.setInt(1, student.getRoll_no());
		statement.setString(2, student.getName());
		statement.setString(3, student.getDob());
		statement.setString(4, student.getCourse());
		statement.setString(5, student.getAddress());
		// New Parameter (Photo Name)
		statement.setString(6, student.getPhotoName());
		boolean rowInserted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();

		return rowInserted;

	}

//show student method
	public List<Student> showAllStudents() throws SQLException {
		List<Student> listStudent = new ArrayList<>();
		String query = "SELECT * FROM students";
		connect();
		Statement stmt = jdbcConnection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			int id = rs.getInt("id");
			int roll_no = rs.getInt("roll_no");
			String dob = rs.getString("dob");
			String name = rs.getString("name");
			String course = rs.getString("course");
			String address = rs.getString("address");

			// --- YE LINE ADD KAREIN (Zaroori Hai) ---
	        String photoName = rs.getString("photo_name");

	        // Student object banate waqt photo set karein
	        Student student = new Student(roll_no, name, dob, address, course);
	        student.setId(id);
	        student.setPhotoName(photoName); // <-- Setter call karna mat bhoolna!

	        listStudent.add(student);
		}
		rs.close();
		stmt.close();
		disconnect();
		return listStudent;
	}
	// Search Method
	public List<Student> searchStudents(String query) {
	    List<Student> students = new ArrayList<>();
	    
	    // Logic: Ya to Name match ho YA Roll No match ho
	    String sql = "SELECT * FROM students WHERE name LIKE ? OR roll_no LIKE ?";
	    
	    try {
	        connect();
	        PreparedStatement pstmt = jdbcConnection.prepareStatement(sql);
	        
	        // Dono jagah same query dalenge, aage piche % laga kar
	        String searchPattern = "%" + query + "%";
	        
	        pstmt.setString(1, searchPattern); // Name ke liye
	        pstmt.setString(2, searchPattern); // Roll No ke liye
	        
	        ResultSet rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            int roll_no = Integer.parseInt(rs.getString("roll_no"));
	            String name = rs.getString("name");
	            String dob = rs.getString("dob");
	            String address = rs.getString("address");
	            String course = rs.getString("course");
	            String photo = rs.getString("photo_name"); // Photo bhi leni hai
	           
	            Student student = new Student(roll_no, name, dob, address, course);
	            student.setId(id);
	            student.setPhotoName(photo);
	            
	            students.add(student);
	        }
	        
	        rs.close();
	        pstmt.close();
	        disconnect();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return students;
	}
//delete student
	public boolean deleteStudent(Student student) throws SQLException {
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
	public boolean updateStudent(Student student) throws SQLException {
		String query = "UPDATE students SET roll_no = ?, dob = ?,name = ?,course = ?,address = ?,photo_name = ? WHERE id = ?";
		connect();
		PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
		pstmt.setInt(1, student.getRoll_no());
		pstmt.setString(2, student.getDob());
		pstmt.setString(3, student.getName());
		pstmt.setString(4, student.getCourse());
		pstmt.setString(5, student.getAddress());
		pstmt.setString(6, student.getPhotoName());
		pstmt.setInt(7, student.getId());

		boolean rowUpdated = pstmt.executeUpdate() > 0;
		pstmt.close();
		disconnect();
		return rowUpdated;
	}
//get one Student

	public Student getStudent(int id) throws SQLException {
		Student student = null;
		String query = "SELECT * FROM students WHERE id = ?";
		connect();

		PreparedStatement pstmt = jdbcConnection.prepareStatement(query);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			int roll_no = rs.getInt("roll_no");
			String dob = rs.getString("dob");
			String name = rs.getString("name");
			String address = rs.getString("address");
			String course = rs.getString("course");
			
			// --- NEW: Photo URL fetch karna mat bhoolna ---
            String photo = rs.getString("photo_name");
			
			student = new Student(id, roll_no, name, dob, address, course);
			student.setId(id);
            student.setPhotoName(photo); // Setter call kiya
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
		if (rs.next()) {
			count = rs.getInt(1);
		}

		rs.close();
		pstmt.close();
		disconnect(); // Agar alag se method hai to call karein, ya connection close karein
		return count;
	}

	// 2. Total Login Users ginnne ke liye (Optional: UserDAO me bhi rakh sakte hain
	// par yahan easy hai)
	public int getTotalUsers() throws SQLException {
		String sql = "SELECT COUNT(*) FROM users"; // Users table count
		connect();
		PreparedStatement pstmt = jdbcConnection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		int count = 0;
		if (rs.next()) {
			count = rs.getInt(1);
		}

		rs.close();
		pstmt.close();
		return count;
	}
}
