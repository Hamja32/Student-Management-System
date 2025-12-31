package com.crud;

public class Result {
	private int id;
	private int student_id;
	private int marks_obtained;
	private int total_marks;
	private String subject;
	
	
	
	private String studentName;
    private String rollNo;
    private String course;
    
    
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Result() {}
	//insertion ke liye
	public Result(int student_id, int marks_obtained, int total_marks, String subject) {
		this.student_id = student_id;
		this.marks_obtained = marks_obtained;
		this.total_marks = total_marks;
		this.subject = subject;
	}
	//updation ke liye
	public Result(int id, int student_id, int marks_obtained, int total_marks, String subject) {
		this.id = id;
		this.student_id = student_id;
		this.marks_obtained = marks_obtained;
		this.total_marks = total_marks;
		this.subject = subject;
	}
	//getter and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getMarks_obtained() {
		return marks_obtained;
	}
	public void setMarks_obtained(int marks_obtained) {
		this.marks_obtained = marks_obtained;
	}
	public int getTotal_marks() {
		return total_marks;
	}
	public void setTotal_marks(int total_marks) {
		this.total_marks = total_marks;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
