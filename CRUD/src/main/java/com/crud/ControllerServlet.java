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

	@Override
	public void init() {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

		studentDAO = new StudentDAO(jdbcURL, jdbcUsername, jdbcPassword);

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
	    if(session == null || session.getAttribute("currentUser") == null) {
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
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listStudent(HttpServletRequest request, HttpServletResponse response)throws SQLException, IOException, ServletException {
		 // 1. Table ka data (Purana code)
	    List<Student> listStudents = studentDAO.showAllStudents();
	    request.setAttribute("students", listStudents);
	    
	    // 2. --- NEW CODE: Dashboard Stats ---
	    int totalStd = studentDAO.getTotalStudents();
	    int totalUsr = studentDAO.getTotalUsers();
	    
	    // JSP ko bhejo
	    request.setAttribute("totalStudents", totalStd);
	    request.setAttribute("totalUsers", totalUsr);
	    
	    // 3. Forward
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
		Student student = new Student(roll_no,name ,dob, address, course);
		studentDAO.insertStudent(student);
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
		Student student = new Student(id, roll_no, name,dob, address, course);
		studentDAO.updateStudent(student);
		response.sendRedirect("list");
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		Student student = new Student(id);
		studentDAO.deleteStudent(student);
		response.sendRedirect("list");

	}
	
}