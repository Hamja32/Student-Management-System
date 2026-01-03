package com.crud;

public class Student {
	private int id;
	private int roll_no;
	private String name;
	private String dob;
	private String address;
	private String course;
	// --- NEW FIELD ADD KAREIN ---
    private String photoName;

    // --- NEW GETTER & SETTER ---
    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
	public Student() {}
	public Student(int id) {
		this.id = id;
		}
	public Student(int id,int roll_no,String name,String dob,String address,String course) {
		this(roll_no,name,dob,address,course);
		this.id = id;
	}
	public Student(int roll_no,String name,String dob,String address,String course) {
		this.dob = dob;
		this.roll_no = roll_no;
		this.name = name;
		this.course = course;
		this.address = address;
	}
	public int getRoll_no() {
		return roll_no;
	}
	public void setRoll_no(int roll_no) {
		this.roll_no = roll_no;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}


}
