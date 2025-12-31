<%@page import="java.util.List"%>
<%@page import="com.crud.Result"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Full Marksheet</title>
<style>
body {
	font-family: sans-serif;
	background-color: #f4f4f4;
	display: flex;
	justify-content: center;
	padding-top: 50px;
}

.sheet-card {
	background: white;
	width: 600px;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 10px 10px 10px 10px rgba(0, 0, 0, 0.1);
}

h2 {
	text-align: center;
	color: #333;
	border-bottom: 2px solid #007bff;
	padding-bottom: 10px;
}

.student-info {
	margin-bottom: 20px;
	font-size: 16px;
}

.student-info table {
	width: 100%;
	border: none;
}

.student-info td {
	padding: 5px;
}

.marks-table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.marks-table th, .marks-table td {
	border: 1px solid #ddd;
	padding: 12px;
	text-align: center;
}

.marks-table th {
	background-color: #007bff;
	color: white;
}

.marks-table tr:nth-child(even) {
	background-color: #f9f9f9;
}

.total-row {
	font-weight: bold;
	background-color: #eef;
}

.status-pass {
	color: green;
	font-weight: bold;
}

.status-fail {
	color: red;
	font-weight: bold;
}
/* 2. Navigation Links (Top) */
.nav-links {
	margin-bottom: 20px;
	text-align: center;
}

.nav-links a {
	text-decoration: none;
	color: #34495e;
	background: #e8e8e8;
	padding: 8px 16px;
	border-radius: 20px;
	font-size: 14px;
	margin: 0 5px;
	transition: 0.3s;
}

.nav-links a:hover {
	background: #d6d6d6;
	color: #000;
}
</style>
</head>
<body>
	<%
	// Agar session null hai ya currentUser nahi hai, to Login par bhaga do
	if (session.getAttribute("currentUser") == null) {
		response.sendRedirect("login.jsp?status=failed");
		return;
	}
	%>

	<div
		style="position: absolute; top: 20px; right: 20px; font-family: sans-serif;">
		<span style="color: #555; margin-right: 15px;"> Hello, <b>${sessionScope.currentUser.email}</b>
		</span> <a href="login"
			style="background-color: #e74c3c; color: white; padding: 8px 15px; text-decoration: none; border-radius: 5px; font-size: 14px;">Logout
			🔒</a>
	</div>
	<%
	// List receive karein
	List<Result> list = (List<Result>) request.getAttribute("resultList");

	// Check karein ki list khali to nahi hai
	if (list != null && !list.isEmpty()) {
		// Student info pehle object se nikal lo
		Result info = list.get(0);
	%>

	<div
		style="display: flex; flex-direction: column; justify-content: space-around; align-items: center; height: 70vh;">

		<div class="nav-links">
			<a href="<%=request.getContextPath()%>/list">← Back to List</a>
		</div>
		<div class="sheet-card">
			<h2>Student Mark Sheet</h2>

			<div class="student-info">
				<table>
					<tr>
						<td><b>Name:</b> <%=info.getStudentName()%></td>
						<td><b>Roll No:</b> <%=info.getRollNo()%></td>
					</tr>
					<tr>
						<td><b>Course:</b> <%=info.getCourse()%></td>
						<td><b>Year:</b> 2024</td>
					</tr>
				</table>
			</div>

			<table class="marks-table">
				<thead>
					<tr>
						<th>Subject</th>
						<th>Total Marks</th>
						<th>Obtained Marks</th>
					</tr>
				</thead>
				<tbody>
					<%
					int grandTotalMarks = 0;
					int grandObtainedMarks = 0;

					// Loop chala kar har subject ki row banayein
					for (Result r : list) {
						grandTotalMarks += r.getTotal_marks();
						grandObtainedMarks += r.getMarks_obtained();
					%>
					<tr>
						<td style="text-align: left; padding-left: 20px;"><%=r.getSubject()%></td>
						<td><%=r.getTotal_marks()%></td>
						<td><%=r.getMarks_obtained()%></td>
					</tr>
					<%
					} // Loop ends here
					%>

					<tr class="total-row">
						<td style="text-align: right; padding-right: 20px;">GRAND
							TOTAL</td>
						<td><%=grandTotalMarks%></td>
						<td><%=grandObtainedMarks%></td>
					</tr>
				</tbody>
			</table>

			<%
			double percentage = ((double) grandObtainedMarks / grandTotalMarks) * 100;
			String status = percentage >= 33 ? "PASS" : "FAIL";
			String statusClass = percentage >= 33 ? "status-pass" : "status-fail";
			%>

			<div style="margin-top: 20px; text-align: right; font-size: 18px;">
				Percentage: <b><%=String.format("%.2f", percentage)%>%</b><br>
				Result Status: <span class="<%=statusClass%>"><%=status%></span>
			</div>

			<br>
			<div align="center">
				<button onclick="window.print()">Print Marksheet</button>
			</div>

		</div>

	</div>
	<%
	} else {
	%>
	<div
		style="display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100px;">
		<div class="nav-links">
			<a href="<%=request.getContextPath()%>/list">← Back to List</a>
		</div>
		<h3>No Results Found for this Student ID</h3>
	</div>
	<%
	}
	%>

</body>
</html>