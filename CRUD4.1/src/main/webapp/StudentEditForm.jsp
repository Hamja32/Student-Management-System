<%@page import="com.crud.Student"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Student Details</title>
<style type="text/css">
/* 1. Global Styles */
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f4f7f6;
	margin: 0;
	padding: 20px;
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	min-height: 100vh;
}

/* 2. Navigation */
.nav-links {
	margin-bottom: 20px;
	text-align: center;
}

.nav-links a {
	text-decoration: none;
	color: #555;
	background: #e8e8e8;
	padding: 8px 16px;
	border-radius: 20px;
	font-size: 14px;
	margin: 0 5px;
	transition: 0.3s;
	border: 1px solid #ddd;
}

.nav-links a:hover {
	background: #d6d6d6;
	color: #000;
	border-color: #bbb;
}

/* 3. Main Card Styling */
.form-card {
	background-color: #ffffff;
	width: 100%;
	max-width: 500px;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
	/* ORANGE Border for EDIT Mode */
	border-top: 5px solid #f39c12;
}

h2 {
	text-align: center;
	color: #2c3e50;
	margin-top: 0;
	margin-bottom: 10px;
}

.subtitle {
	text-align: center;
	color: #7f8c8d;
	font-size: 14px;
	margin-bottom: 30px;
	display: block;
}

/* 4. Form Elements */
.form-group {
	margin-bottom: 20px;
}

label {
	display: block;
	margin-bottom: 8px;
	font-weight: 600;
	color: #34495e;
	font-size: 14px;
}

input[type="text"], input[type="url"], input[type="date"], textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #ddd;
	border-radius: 6px;
	font-size: 15px;
	box-sizing: border-box;
	transition: border-color 0.3s;
	font-family: inherit;
	background-color: #fff;
}

/* Focus Effect - Orange Tint */
input:focus, textarea:focus {
	border-color: #f39c12;
	outline: none;
	box-shadow: 0 0 5px rgba(243, 156, 18, 0.2);
}

textarea {
	resize: vertical;
	min-height: 80px;
}

/* 5. Update Button - Orange */
.btn-update {
	width: 100%;
	padding: 14px;
	background-color: #f39c12; /* Orange Color */
	color: white;
	border: none;
	border-radius: 6px;
	font-size: 16px;
	font-weight: bold;
	cursor: pointer;
	transition: background-color 0.3s;
	margin-top: 10px;
}

.btn-update:hover {
	background-color: #e67e22; /* Darker Orange */
}
</style>
</head>
<body>

	<div class="nav-links">
		<a href="<%=request.getContextPath()%>/list"> ← Cancel & Back to
			List</a>
	</div>

	<div class="form-card">
		<h2>Edit Student</h2>
		<span class="subtitle">Update details for existing student</span>

		<form action="<%=request.getContextPath()%>/update" method="post">

			<input type="hidden" name="id" value="${student.getId()}" />
			<div class="form-group">
				<label>Upload Photo:</label> <input type="url" name="photo"
					placeholder="Paste Image Link Here (https://...)"
					style="width: 80%;" value="${student.getPhotoName()}" required />
					<img src="${student.getPhotoName()}" width="80" height="80" style="border-radius: 50%;" 	onerror="this.src='https://cdn-icons-png.flaticon.com/512/149/149071.png';" >



			</div>
			<div class="form-group">
				<label>Roll Number</label> <input type="text" name="roll_no"
					value="${student.getRoll_no()}" required />
			</div>

			<div class="form-group">
				<label>Full Name</label> <input type="text" name="name"
					value="${student.getName()}" required />
			</div>

			<div class="form-group">
				<label>Date of Birth</label> <input type="date" name="dob"
					value="${student.getDob()}" required />
			</div>

			<div class="form-group">
				<label>Address</label>
				<textarea name="address" required>${student.getAddress()}</textarea>
			</div>

			<div class="form-group">
				<label>Course</label> <input type="text" name="course"
					value="${student.getCourse()}" required />
			</div>

			<input type="submit" value="Update Details" class="btn-update" />

		</form>
	</div>

</body>
</html>