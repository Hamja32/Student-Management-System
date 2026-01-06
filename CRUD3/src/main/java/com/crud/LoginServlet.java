package com.crud;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() {
		userDAO = new UserDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			// 1. DAO se pucho user sahi hai kya?
			User user = userDAO.checkLogin(email, password);

			if (user != null) {
				// --- LOGIN SUCCESS ---

				// SESSION banana (Matlb server ab user ko yaad rakhega)
				HttpSession session = request.getSession();
				session.setAttribute("currentUser", user);

				// 2. Role Check karo (Traffic Police Logic)
				if (user.getRole().equals("teacher")) {

					// Teacher hai -> List page par bhejo
					response.sendRedirect("list");

				} else {

					// Student hai -> Uske khud ke Result page par bhejo
					// Note: Hum user.getStudentId() use kar rahe hain jo DB se aaya
					int myId = user.getStudentId();

					// Agar student id valid hai
					if(myId > 0) {
						response.sendRedirect("view?id=" + myId);
					} else {
						// Agar student hai par ID link nahi hai (Error case)
						response.sendRedirect("login.jsp?error=noid");
					}
				}

			} else {
				// --- LOGIN FAILED ---
				// Wapas login page par bhejo error ke sath
				response.sendRedirect("login.jsp?status=failed");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Logout ke liye bhi method chahiye hoga (Bonus)
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate(); // Session khatam
		}
		response.sendRedirect("login.jsp?status=logout");
	}
}