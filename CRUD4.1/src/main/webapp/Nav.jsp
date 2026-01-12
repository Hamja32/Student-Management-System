<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<style>
* {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
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
	margin-bottom: 31px;
	border: 1px solid gray;
	padding-bottom: 29px;
	padding-top: 27px;
	padding-left: 20px;
	background: rgba(00, 55, 55, 0.75);
	backdrop-filter: blur(15px);
	-webkit-backdrop-filter: blur(15px);
	border-bottom: 1px solid rgba(255, 255, 255, 0.2);
	position: sticky;
	top: 0;
	z-index: 1000;
	border-radius:5px;
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
	background-color: white;
	color:black;
}
.btn-report{
	background-color:teal;
}
.btn-add {
	background-color: #27ae60; /* Green for Add */
}
.btn-dashboard{
	background-color:blue;
}
.btn-dashbaord:hover{
background-color:aqua;
}
.btn-manage-result {
	background: black;
	margin-top: 5px;
}
.btn-attendance{
background-color:orange;
}
.btn-attendance:hover{
background-color: #ffc00;
}

.allLinks {
	padding-right: 20px;
}
</style>
<body>
	<div class="nav-bar"
		style="display: flex; justify-content: space-between; align-items: center;">
		<div>
			<span style="font-weight: bold; color: #fff;">Welcome,
				${sessionScope.currentUser.email} (Teacher)</span>
		</div>

		<div class="allLinks">
			<a href="<%=request.getContextPath()%>/list" class="btn-nav">
				Show Student</a> <a href="<%=request.getContextPath()%>/new"
				class="btn-nav btn-add">+ Add Student</a> <a
				href="<%=request.getContextPath()%>/attendance" class="btn-nav btn-attendance">Mark
				Attendance 📅</a><a
				href="<%=request.getContextPath()%>/view-attendance"
				class="btn-nav btn-report">View Reports 📊</a><a
				href="<%=request.getContextPath()%>/dashboard"
				class="btn-nav btn-dashboard">Dashboard</a><a href="login"
				class="btn-nav" style="background-color: #e74c3c;">Logout</a>
		</div>
	</div>
</body>
</html>