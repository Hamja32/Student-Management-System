package com.crud;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDAO studentDAO;
	private AttendanceDAO attendanceDAO;

	@Override
	public void init() {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

		studentDAO = new StudentDAO(jdbcURL, jdbcUsername, jdbcPassword);
		attendanceDAO = new AttendanceDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// --- SECURITY BLOCK START ---
		HttpSession session = request.getSession(false); // false mtlb naya session mat banao, purana check karo

		// 1. Check Login
		if (session == null || session.getAttribute("currentUser") == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		User user = (User) session.getAttribute("currentUser");
		String actions = request.getServletPath();

		// 2. Check Role Logic
		// Agar action Result dekhne ka nahi hai (mtlb list, delete, edit hai)
		// AUR user Teacher nahi hai -> To block karo
		if (!actions.equals("/view") && !user.getRole().equals("teacher")) {
			// Error page par bhejo ya Access Denied bolo
			response.sendRedirect("login.jsp?error=accessdenied");
			return;
		}
		// --- SECURITY BLOCK END ---
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/list":
				listStudent(request, response);
				break;
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertStudent(request, response);
				break;
			case "/delete":
				deleteStudent(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/view":
				viewStudentResult(request, response);
				break;
			case "/update":
				updateStudent(request, response);
				break;
			case "/manage-result":
				manageResult(request, response);
				break;

			case "/save-result":
				saveResult(request, response);
				break;
			case "/attendance":
				showAttendanceForm(request, response);
				break;
			case "/save-attendance":
				saveAttendance(request, response);
				break;
			case "/view-attendance":
				viewAttendance(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		// 1. Table ka data (Purana code)
		// 1. Check karo user ne search box me kuch likha hai kya?
		String searchQuery = request.getParameter("search");

		List<Student> listStudents;

		// 2. Agar Search Query hai aur khali nahi hai
		if (searchQuery != null && !searchQuery.trim().isEmpty()) {
			// Search wala logic
			listStudents = studentDAO.searchStudents(searchQuery);
		} else {
			// Normal (Purana) logic
			listStudents = studentDAO.showAllStudents();
		}

		// 3. Wahi purana kaam (Dashboard numbers + JSP forwarding)
		request.setAttribute("students", listStudents);

		// Dashboard Stats (Agar aapne phase 2 me banaye the)
		request.setAttribute("totalStudents", studentDAO.getTotalStudents());
		request.setAttribute("totalUsers", studentDAO.getTotalUsers());

		RequestDispatcher dispatcher = request.getRequestDispatcher("ShowStudent.jsp");
		dispatcher.forward(request, response);
	}

	// ye hai student result ke liye........................
	private void viewStudentResult(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));

		// Ab list aayegi
		List<Result> resultList = studentDAO.getStudentResult(id);

		// 'resultList' naam se set karein
		request.setAttribute("resultList", resultList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ShowResult.jsp");
		dispatcher.forward(request, response);
	}

	private void manageResult(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		int studentId = Integer.parseInt(request.getParameter("id"));

		// 1. Check karo is student ke marks pehle se hain kya?
		List<Result> existingResults = studentDAO.getResultsByStudentId(studentId);

		// 2. Student ki details bhi nikalo (Naam dikhane ke liye)
		Student student = studentDAO.getStudent(studentId);

		// Data ko request me set karo
		request.setAttribute("student", student);

		if (existingResults != null && !existingResults.isEmpty()) {
			// Marks hain -> EDIT MODE
			request.setAttribute("results", existingResults);
			request.setAttribute("isEdit", true);
		} else {
			// Marks nahi hain -> INSERT MODE
			String[] defaultSubjects = {"English", "Hindi", "Maths", "Science", "Social Science", "Urdu"};
			request.setAttribute("subjects", defaultSubjects);
			request.setAttribute("isEdit", false);
		}

		// JSP par bhejo
		RequestDispatcher dispatcher = request.getRequestDispatcher("ResultForm.jsp");
		dispatcher.forward(request, response);
	}

	private void saveResult(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

		int studentId = Integer.parseInt(request.getParameter("studentId"));
		boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));

		// JSP se 'count' hidden field se pata chalega kitne subjects hain
		int count = Integer.parseInt(request.getParameter("count"));

		for (int i = 0; i < count; i++) {
			// Loop chala kar har subject ka data nikalo: subject_0, marks_0, etc.
			String subject = request.getParameter("subject_" + i);
			int marks = Integer.parseInt(request.getParameter("marks_" + i));

			if (isEdit) {
				// Agar Edit mode hai, to Result ki ID bhi chahiye
				int resultId = Integer.parseInt(request.getParameter("id_" + i));
				studentDAO.updateSingleResult(resultId, marks);
			} else {
				// Agar Insert mode hai, to naya object banao
				Result res = new Result();
				res.setStudent_id(studentId);
				res.setSubject(subject);
				res.setTotal_marks(100);
				res.setMarks_obtained(marks);
				studentDAO.insertResult(res);
			}
		}

		// Kaam hone par List par wapas bhejo
		response.sendRedirect("list");
	}

	// result function end here
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("StudentForm.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Student existingStudent = studentDAO.getStudent(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("StudentEditForm.jsp");
		request.setAttribute("student", existingStudent);
		dispatcher.forward(request, response);

	}

	private void insertStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String dob = request.getParameter("dob");
		String name = request.getParameter("name");
		String course = request.getParameter("course");
		String address = request.getParameter("address");
		int roll_no = Integer.parseInt(request.getParameter("roll_no"));

		// 2. Simply URL string get karo (Jaise Name get kiya tha)
		String photoUrl = request.getParameter("photo");
		// Validation: Agar user ne URL nahi dala to ek default internet image laga do
		if (photoUrl == null || photoUrl.trim().isEmpty()) {
			photoUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png"; // Default User Icon
		}
		// Student Object banakar Database me save karna (Purana code)
		Student newStudent = new Student(roll_no, name, dob, address, course);
		newStudent.setPhotoName(photoUrl);
		studentDAO.insertStudent(newStudent);

		response.sendRedirect("list");
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String dob = request.getParameter("dob");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String course = request.getParameter("course");
		int roll_no = Integer.parseInt(request.getParameter("roll_no"));
		String photoUrl = request.getParameter("photo");

		// Validation: Agar user URL khali chhod de, to purana wala hi rehne do
		// (Note: Iske liye thoda advance logic lagta hai, abhi ke liye maan lete hain
		// user URL dega)
		if (photoUrl == null || photoUrl.trim().isEmpty()) {
			photoUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
		}
		Student student = new Student(id, roll_no, name, dob, address, course);

		student.setId(id);
		student.setPhotoName(photoUrl); // Set URL
		studentDAO.updateStudent(student);
		response.sendRedirect("list");

	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		Student student = new Student(id);
		studentDAO.deleteStudent(student);
		response.sendRedirect("list");

	}
	//attendance
	
	private void showAttendanceForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException, SQLException {
	    
	    // Hamein saare students chahiye list dikhane ke liye
	    List<Student> listStudent = studentDAO.showAllStudents();
	    request.setAttribute("students", listStudent);
	    
	    RequestDispatcher dispatcher = request.getRequestDispatcher("Attendance.jsp");
	    dispatcher.forward(request, response);
	}
	
	// Route: "/save-attendance"
	// Route: "/save-attendance"
	private void saveAttendance(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException, SQLException {
	    
	    String date = request.getParameter("attendanceDate");
	    
	    // 1. Students list nikalo (IDs ke liye)
	    List<Student> students = studentDAO.showAllStudents();
	    
	    for (Student s : students) {
	        int id = s.getId();
	        String status = request.getParameter("status_" + id);
	        
	        if (status != null) {
	            // --- SMART LOGIC START ---
	            
	            // Check karo: Kya attendance pehle se hai?
	            boolean exists = attendanceDAO.isAttendanceMarked(id, date);
	            
	            if (exists) {
	                // CASE A: Agar hai -> UPDATE karo (Correction)
	                attendanceDAO.updateAttendance(id, date, status);
	            } else {
	                // CASE B: Agar nahi hai -> INSERT karo (New Entry)
	                attendanceDAO.markAttendance(id, date, status);
	            }
	            
	            // --- SMART LOGIC END ---
	        }
	    }
	    
	    // Success message ke sath wapas bhejo (Optional URL parameter)
	    response.sendRedirect("view-attendance?date=" + date); 
	}
	
	
	//view attendance
	// Route: "/view-attendance"
	private void viewAttendance(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    
	    String selectedDate = request.getParameter("date");
	    
	    // Agar user ne date select ki hai
	    if (selectedDate != null && !selectedDate.isEmpty()) {
	        List<AttendanceDTO> list = attendanceDAO.getAttendanceByDate(selectedDate);
	        request.setAttribute("attendanceList", list);
	        request.setAttribute("selectedDate", selectedDate); // Taaki date box me wahi date dikhe
	    }
	    
	    RequestDispatcher dispatcher = request.getRequestDispatcher("ViewAttendance.jsp");
	    dispatcher.forward(request, response);
	}
}