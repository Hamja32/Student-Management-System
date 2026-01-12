<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register User</title>
<style>
    /* Global Styles */
    body {
        font-family: 'Segoe UI', sans-serif;
        background-color: #f4f7f6;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }

    /* Card Design */
    .register-card {
        background: white;
        padding: 40px;
        width: 100%;
        max-width: 400px;
        border-radius: 10px;
        box-shadow: 0 10px 25px rgba(0,0,0,0.1);
        border-top: 5px solid #27ae60; /* Green Border */
    }

    h2 { text-align: center; color: #333; margin-top: 0; }

    /* Form Elements */
    .form-group { margin-bottom: 20px; }
    
    label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; }
    
    input, select {
        width: 100%;
        padding: 12px;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-sizing: border-box; /* Fix padding issue */
        font-size: 15px;
    }

    input:focus, select:focus {
        border-color: #27ae60;
        outline: none;
    }

    /* Button */
    .btn-register {
        width: 100%;
        padding: 12px;
        background-color: #27ae60;
        color: white;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        font-weight: bold;
    }
    .btn-register:hover { background-color: #219150; }

    /* Login Link */
    .login-link {
        text-align: center;
        margin-top: 15px;
        font-size: 14px;
    }
    .login-link a { color: #27ae60; text-decoration: none; }

    /* Hidden Section for Student ID */
    #studentIdSection { display: none; } 
</style>
</head>
<body>

<div class="register-card">
    <h2>Create Account</h2>
    
    <form action="<%= request.getContextPath() %>/register" method="post">
        
        <div class="form-group">
            <label>Email Address</label>
            <input type="email" name="email" placeholder="Enter email" required>
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" placeholder="Create password" required>
        </div>

        <div class="form-group">
            <label>I am a...</label>
            <select name="role" id="roleSelect" onchange="toggleStudentId()">
                <option value="teacher">Teacher</option>
                <option value="student">Student</option>
            </select>
        </div>

        <div class="form-group" id="studentIdSection">
            <label>Your Student ID</label>
            <input type="number" name="studentId" placeholder="Enter your ID from student list (e.g. 101)">
            <small style="color:red; font-size:12px;">*Check list for your ID</small>
        </div>

        <button type="submit" class="btn-register">Sign Up</button>
        
        <div class="login-link">
            Already have an account? <a href="login.jsp">Login here</a>
        </div>
    </form>
</div>

<script>
    function toggleStudentId() {
        var role = document.getElementById("roleSelect").value;
        var section = document.getElementById("studentIdSection");

        if (role === "student") {
            section.style.display = "block"; // Student hai to dikhao
        } else {
            section.style.display = "none";  // Teacher hai to chupao
            document.getElementsByName("studentId")[0].value = ""; // Clear value
        }
    }
</script>

</body>
</html>