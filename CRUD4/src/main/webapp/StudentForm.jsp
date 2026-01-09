<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Add New Student</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
/* 1. Global Reset & Fonts */
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f4f7f6;
	margin: 0;
	padding: 20px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	min-height: 100vh;
	flex-direction: column;
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

/* 3. Main Form Card */
.form-card {
	background-color: #ffffff;
	width: 100%;
	max-width: 500px; /* Form zyada chouda na ho */
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
	border-top: 5px solid #27ae60; /* Green Top Border */
}

h2 {
	text-align: center;
	color: #2c3e50;
	margin-top: 0;
	margin-bottom: 30px;
}

/* 4. Form Groups (Labels + Inputs) */
.form-group {
	margin-bottom: 20px;
}

label {
	display: block;
	margin-bottom: 8px;
	font-weight: 600;
	color: #555;
	font-size: 14px;
}

input[type="text"], input[type="url"], input[type="date"], textarea {
	width: 100%;
	padding: 12px;
	border: 1px solid #ddd;
	border-radius: 6px;
	font-size: 15px;
	box-sizing: border-box; /* Padding width me jud jaye */
	transition: border-color 0.3s;
	font-family: inherit;
}

/* Focus Effect (Jab click karein) */
input:focus, textarea:focus {
	border-color: #27ae60;
	outline: none;
	box-shadow: 0 0 5px rgba(39, 174, 96, 0.2);
}

textarea {
	resize: vertical; /* Sirf height badha sakein */
	min-height: 80px;
}

/* 5. Submit Button */
.btn-submit {
	width: 100%;
	padding: 14px;
	background-color: #27ae60;
	color: white;
	border: none;
	border-radius: 6px;
	font-size: 16px;
	font-weight: bold;
	cursor: pointer;
	transition: background-color 0.3s;
	margin-top: 10px;
}

.btn-submit:hover {
	background-color: #219150;
}

/* --- TOAST NOTIFICATION CSS --- */
#toast {
	visibility: hidden;
	min-width: 300px;
	background-color: #333; /* Default Dark */
	color: #fff;
	text-align: center;
	border-radius: 50px; /* Pill shape */
	padding: 16px;
	position: fixed;
	z-index: 100;
	left: 50%;
	bottom: 30px;
	transform: translateX(-50%);
	font-size: 16px;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

/* Success Theme for Toast */
#toast.success {
	background-color: #27ae60; /* Green */
}

#toast.show {
	visibility: visible;
	animation: fadein 0.5s, fadeout 0.5s 2.5s forwards;
}

@
keyframes fadein {
	from {bottom: 0;
	opacity: 0;
}

to {
	bottom: 30px;
	opacity: 1;
}

}
@
keyframes fadeout {
	from {bottom: 30px;
	opacity: 1;
}

to {
	bottom: 0;
	opacity: 0;
}
}
</style>
</head>
<body>
>
	<div class="nav-links">
		<a href="<%=request.getContextPath()%>/list">← Back to Student
			List</a>
	</div>

	<div id="toast">Student added successfully!</div>

	<div class="form-card">
		<h2>Add New Student</h2>

		<form action="<%=request.getContextPath()%>/insert" method="post"
			onsubmit="return handleFormSubmit()">
			<div class="form-group">
				<label>Upload Photo:</label>
				<input type="url" name="photo" placeholder="Paste Image Link Here (https://...)" style="width: 100%;" required />
			</div>
			<div class="form-group">
				<label>Roll Number</label> <input type="text" name="roll_no"
					placeholder="e.g. 101" required />
			</div>

			<div class="form-group">
				<label>Full Name</label> <input type="text" name="name"
					placeholder="Enter student name" required />
			</div>

			<div class="form-group">
				<label>Date of Birth</label> <input type="date" name="dob" required />
			</div>

			<div class="form-group">
				<label>Address</label>
				<textarea name="address" placeholder="Enter full address" required></textarea>
			</div>

			<div class="form-group">
				<label>Course</label> <input type="text" name="course"
					placeholder="e.g. B.Tech CS" required />
			</div>

			<input type="submit" value="Save Student" class="btn-submit" />

		</form>
	</div>

	<script>
		// Note: Asli project me form submit hone par page reload hota hai, 
		// isliye Toast tabhi dikhega agar aap AJAX use karein ya redirect ke baad parameter check karein.
		// Lekin demo ke liye ye animation yahan hai:

		function handleFormSubmit() {
			// Toast element lo
			var x = document.getElementById("toast");

			// Success styling add karo
			x.classList.add("success");

			// Show class add karo
			x.className = "show success";

			// Form submit hone dein (Return true)
			// Agar aapko sirf demo dekhna hai bina server submit ke to 'return false' karein
			return true;
		}

		// Agar URL me ?success=true aata hai (server redirect ke baad) to toast dikhao
		window.onload = function() {
			const urlParams = new URLSearchParams(window.location.search);
			if (urlParams.get('success') === 'true') {
				var x = document.getElementById("toast");
				x.innerHTML = "Student Saved Successfully!";
				x.className = "show success";
				setTimeout(function() {
					x.className = x.className.replace("show", "");
				}, 3000);
			}
		}
	</script>

</body>
</html>