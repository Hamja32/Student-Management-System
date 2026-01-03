<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login - Student Portal</title>
<style>
    /* Global Styles */
    body {
        font-family: 'Segoe UI', sans-serif;
        background-color: #eef2f3;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }

    /* Login Card - BLUE Theme */
    .login-card {
        background: white;
        padding: 40px;
        width: 100%;
        max-width: 380px; /* Thoda compact */
        border-radius: 12px;
        box-shadow: 0 15px 30px rgba(0,0,0,0.1);
        border-top: 5px solid #3498db; /* Blue Top Border */
        text-align: center;
    }

    h2 { color: #2c3e50; margin-bottom: 30px; }

    /* Inputs */
    .form-group { margin-bottom: 20px; text-align: left; }
    
    label { display: block; margin-bottom: 8px; font-weight: 600; color: #555; font-size: 14px; }
    
    input {
        width: 100%;
        padding: 12px;
        border: 1px solid #ddd;
        border-radius: 6px;
        box-sizing: border-box;
        font-size: 15px;
        transition: 0.3s;
    }

    input:focus {
        border-color: #3498db;
        outline: none;
        box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
    }

    /* Button */
    .btn-login {
        width: 100%;
        padding: 12px;
        background-color: #3498db;
        color: white;
        border: none;
        border-radius: 6px;
        font-size: 16px;
        font-weight: bold;
        cursor: pointer;
        transition: 0.3s;
        margin-top: 10px;
    }
    .btn-login:hover { background-color: #2980b9; }

    /* Links */
    .links { margin-top: 20px; font-size: 14px; color: #666; }
    .links a { color: #3498db; text-decoration: none; font-weight: 600; }
    .links a:hover { text-decoration: underline; }

    /* --- TOAST NOTIFICATION (Error wala) --- */
    #toast {
        visibility: hidden;
        min-width: 300px;
        background-color: #e74c3c; /* Red for Error */
        color: white;
        text-align: center;
        border-radius: 50px;
        padding: 15px;
        position: fixed;
        z-index: 100;
        left: 50%;
        bottom: 30px;
        transform: translateX(-50%);
        box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        font-weight: 500;
    }

    #toast.show {
        visibility: visible;
        animation: fadein 0.5s, fadeout 0.5s 2.5s forwards;
    }
    
    /* Logout Success Green Color */
    #toast.success { background-color: #27ae60; }

    @keyframes fadein { from {bottom: 0; opacity: 0;} to {bottom: 30px; opacity: 1;} }
    @keyframes fadeout { from {bottom: 30px; opacity: 1;} to {bottom: 0; opacity: 0;} }

</style>
</head>
<body>

    <div id="toast">Invalid Email or Password!</div>

    <div class="login-card">
        <h2>Student Portal Login</h2>
        
        <form action="<%= request.getContextPath() %>/login" method="post">
            
            <div class="form-group">
                <label>Email Address</label>
                <input type="email" name="email" placeholder="Enter your email" required>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn-login">Secure Login</button>
            
            <div class="links">
                New User? <a href="register.jsp">Create an Account</a>
            </div>

        </form>
    </div>

    <script>
        // URL se check karna ki Error hai ya Logout
        window.onload = function() {
            const urlParams = new URLSearchParams(window.location.search);
            var x = document.getElementById("toast");

            if(urlParams.get('status') === 'failed'){
                // Login Fail (Red Toast)
                x.innerHTML = "❌ Invalid Email or Password!";
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
            } 
            else if(urlParams.get('status') === 'logout'){
                // Logout Success (Green Toast)
                x.innerHTML = "✅ Logout Successfully!";
                x.classList.add("success");
                x.classList.add("show");
                setTimeout(function(){ x.className = ""; }, 3000); // Reset classes
            }else if(urlParams.get('status') === 'login'){
            	// Login success (green toast)
            	x.innerHTML = "✅ Login Successfully";
            	x.classList.add("success");
            	x.classList.add("show");
            	setTimeout(() => {
					x.className = "";
				}, 3000);
            }else{
            	// Login success (green toast)
            	x.innerHTML = "✅ nothing";
            }
        }
    </script>

</body>
</html>