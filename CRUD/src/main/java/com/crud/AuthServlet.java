package com.crud;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Ye URL wahi hai jo form ke action me diya tha
@WebServlet("/register")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	public void init() {
		userDAO = new UserDAO();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 1. Form se data nikalna
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		String studentIdStr = request.getParameter("studentId"); // Ye string me aata hai

		// 2. Student ID ko handle karna (Logic)
		int studentId = 0;
		
		// Agar studentIdStr null nahi hai aur khali nahi hai, tabhi parse karo
		if (studentIdStr != null && !studentIdStr.isEmpty()) {
			try {
				studentId = Integer.parseInt(studentIdStr);
			} catch (NumberFormatException e) {
				studentId = 0; // Agar koi gabad value aayi to 0 kar do
			}
		}

		// 3. User Object banana
		User newUser = new User(email, password, role, studentId);

		try {
			// 4. DAO ko call karke save karna
			userDAO.registerUser(newUser);
			
			// 5. Success hone par wapas bhejna (Toast dikhane ke liye)
			response.sendRedirect("register.jsp?success=true");
			
		} catch (SQLException e) {
			e.printStackTrace();
			// Agar error aaye to error page ya wapas form par bhejo
			response.sendRedirect("register.jsp?error=true");
		}
	}
}