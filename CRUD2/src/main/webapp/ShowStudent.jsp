<%@page import="com.crud.Student"%>
<%@page import="java.util.List"%>
<%@page import="com.crud.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Student Management Dashboard</title>
<style type="text/css">
/* 1. Global Settings */
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f4f7f6;
	margin: 0;
	padding: 20px;
}

/* 2. Main Container */
.container {
	max-width: 1200px;
	margin: 0 auto;
	background-color: #ffffff;
	padding: 30px;
	border-radius: 8px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 3. Header Styling */
h1 {
	color: #2c3e50;
	text-align: center;
	margin-bottom: 10px;
}

/* 4. Top Navigation Buttons */
.nav-bar {
	text-align: right;
	margin-bottom: 20px;
	border-bottom: 2px solid #eee;
	padding-bottom: 20px;
}

.btn-nav {
	background-color: #34495e;
	color: white;
	padding: 10px 20px;
	text-decoration: none;
	border-radius: 5px;
	font-size: 14px;
	margin-left: 10px;
	transition: 0.3s;
}

.btn-nav:hover {
	background-color: #2c3e50;
}

.btn-add {
	background-color: #27ae60; /* Green for Add */
}
.btn-manage-result{
	background: black;
	margin-top:5px;
}
.btn-add:hover {
	background-color: #219150;
}

/* 5. Table Styling */
table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
	font-size: 15px;
}

th {
	background-color: #3498db; /* Header Blue */
	color: white;
	padding: 15px;
	text-align: left;
}

td {
	padding: 12px 15px;
	border-bottom: 1px solid #ddd;
	color: #555;
}
/* Zebra Striping */
tr:nth-child(even) {
	background-color: #f9f9f9;
}
/* Hover Effect */
tr:hover {
	background-color: #f1f1f1;
}

/* 6. Action Buttons inside Table */
.action-links a {
	padding: 6px 12px;
	text-decoration: none;
	color: white;
	border-radius: 4px;
	font-size: 12px;
	margin-right: 5px;
	display: inline-block;
}

.btn-edit {
	background-color: #f39c12; /* Orange */
}

.btn-view {
	background-color: #8e44ad; /* Purple */
}

.btn-delete {
	background-color: #e74c3c; /* Red */
}

/* Hover effects for buttons */
.btn-edit:hover {
	background-color: #d35400;
}

.btn-view:hover {
	background-color: #732d91;
}

.btn-delete:hover {
	background-color: #c0392b;
}
/* Dashboard Cards Container */
.dashboard-stats {
	display: flex;
	justify-content: space-between;
	margin-bottom: 30px;
	gap: 20px;
}

/* Individual Card Design */
.stat-card {
	background: white;
	flex: 1; /* Teeno barabar jagah lenge */
	padding: 20px;
	border-radius: 10px;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
	display: flex;
	align-items: center;
	border: 1px solid black; /* Default Color */
}

.stat-card:hover {
	background-color: lime;
	cursor: pointer;
}

/* Card Colors */
.card-blue {
	border-left-color: #3498db;
}

.card-green {
	border-left-color: #27ae60;
}

.card-orange {
	border-left-color: #f39c12;
}

/* Icon and Text Styling */
.stat-icon {
	font-size: 30px;
	margin-right: 15px;
	color: #555;
}

.stat-info h3 {
	margin: 0;
	font-size: 28px;
	color: #333;
}

.stat-info p {
	margin: 0;
	color: #777;
	font-size: 14px;
	text-transform: uppercase;
}
</style>
</head>
<body>
	<%
	// Import User class

	User currentUser = (User) session.getAttribute("currentUser");

	// 1. Login Check: Agar login nahi hai -> Login page par bhejo
	if (currentUser == null) {
		response.sendRedirect("login.jsp");
		return;
	}

	// 2. Role Check: Agar login hai par TEACHER NAHI hai -> Wapas Result par bhej do
	if (!currentUser.getRole().equals("teacher")) {
		// Student ko wapas uske result page par redirect kar do
		response.sendRedirect("view?id=" + currentUser.getStudentId());
		return;
	}
	%>

	<div class="nav-bar"
		style="display: flex; justify-content: space-between; align-items: center;">
		<div>
			<span style="font-weight: bold; color: #2c3e50;">Welcome,
				${sessionScope.currentUser.email} (Teacher)</span>
		</div>

		<div>
			<a href="<%=request.getContextPath()%>/list" class="btn-nav">Refresh
				List</a> <a href="<%=request.getContextPath()%>/new"
				class="btn-nav btn-add">+ Add Student</a> <a href="login"
				class="btn-nav" style="background-color: #e74c3c;">Logout</a>
		</div>
	</div>
	<div class="container">

		<h1>Student Management System</h1>

		<div class="dashboard-stats">

			<div class="stat-card card-blue">
				<div class="stat-icon">🎓</div>
				<div class="stat-info">
					<h3>${totalStudents}</h3>
					<p>Total Students</p>
				</div>
			</div>

			<div class="stat-card card-green">
				<div class="stat-icon">👤</div>
				<div class="stat-info">
					<h3>${totalUsers}</h3>
					<p>Active Users</p>
				</div>
			</div>

			<div class="stat-card card-orange">
				<div class="stat-icon">🏆</div>
				<div class="stat-info">
					<h3>2024</h3>
					<p>Academic Year</p>
				</div>
			</div>

		</div>
		<div style="margin-bottom: 20px; text-align: right;">
			<form action="list" method="get">
				<input type="text" name="search"
					placeholder="Search by Name or Roll No..."
					style="padding: 8px; width: 250px; border: 1px solid #ccc; border-radius: 4px;">

				<button type="submit"
					style="padding: 8px 15px; background-color: #4a90e2; color: white; border: none; border-radius: 4px; cursor: pointer;">
					Search 🔍</button>

				<a href="list"
					style="margin-left: 10px; text-decoration: none; color: #555;">Clear</a>
			</form>
		</div>
		<table>
			<thead>
				<tr>
					<th width="5%">ID</th>
					<th width="10%">Photo</th>
					<th width="10%">Roll No</th>
					<th width="15%">Name</th>
					<th width="15%">DOB</th>
					<th width="15%">Address</th>
					<th width="10%">Class</th>
					<th width="40%">Actions</th>
				</tr>
			</thead>
			<tbody>
				<%
				// List type casting me warning hatane ke liye @SuppressWarnings use kar sakte hain
				List<Student> allstudents = (List<Student>) request.getAttribute("students");

				if (allstudents != null) {
					for (Student student : allstudents) {
				%>
				<tr>
					<td><%=student.getId()%></td>
					<td><img src="<%=student.getPhotoName()%>" width="80"
						height="80" style="border-radius: 50%; object-fit: cover;"
						alt="Student Photo"
						onerror="this.src='https://cdn-icons-png.flaticon.com/512/149/149071.png';" /></td>
					<td><%=student.getRoll_no()%></td>
					<td style="font-weight: bold; color: #333;"><%=student.getName()%></td>
					<td><%=student.getDob()%></td>
					<td><%=student.getAddress()%></td>
					<td><span
						style="background: #e8f4f8; color: #007bff; padding: 3px 8px; border-radius: 10px; font-size: 14px;"><%=student.getCourse()%></span></td>

					<td class="action-links"><a
						href="<%=request.getContextPath()%>/edit?id=<%=student.getId()%>"
						class="btn-edit">Edit</a> <a
						href="<%=request.getContextPath()%>/view?id=<%=student.getId()%>"
						class="btn-view">Result</a> <a
						href="<%=request.getContextPath()%>/delete?id=<%=student.getId()%>"
						class="btn-delete"
						onclick="return confirm('Are you sure you want to delete this student?');">Delete</a>
					<a href="<%=request.getContextPath()%>/manage-result?id=<%=student.getId()%>" class="btn-manage-result">Manage Result</a></td>
				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="7" align="center">No Students Found</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>

	</div>

</body>
</html>