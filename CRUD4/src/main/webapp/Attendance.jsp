<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.crud.Student" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
<title>Mark Attendance</title>
<style>
    body { font-family: sans-serif; padding: 20px; background-color: #f8f9fa; }
    .container { background: white; padding: 20px; border-radius: 8px; width: 70%; margin: auto; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
    th { background-color: #2c3e50; color: white; }
    .btn-save { background-color: #27ae60; color: white; padding: 10px 20px; border: none; cursor: pointer; font-size: 16px; margin-top: 20px; }
    /* Radio Button styling */
    input[type="radio"] { transform: scale(1.5); margin: 0 5px; cursor: pointer; }
    .status-p { color: green; font-weight: bold; }
    .status-a { color: red; font-weight: bold; }
</style>
</head>
<body>

<%
    // Controller se Student List nikalo
    List<Student> students = (List<Student>) request.getAttribute("students");
    
    // Aaj ki date default set karne ke liye
    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<jsp:include page="Nav.jsp" />
<div class="container">
    <h2 style="text-align: center;">Mark Attendance 📅</h2>
    
    <form action="save-attendance" method="post">
        
        <div style="text-align: center; margin-bottom: 15px;">
            <label><b>Select Date:</b></label>
            <input type="date" name="attendanceDate" value="<%= today %>" required style="padding: 5px;">
        </div>

        <table>
            <thead>
                <tr>
                    <th>Roll No</th>
                    <th>Student Name</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <% 
                if (students != null) {
                    for (Student s : students) {
            %>
                <tr>
                    <td><%= s.getRoll_no() %></td>
                    <td><%= s.getName() %></td>
                    <td>
                        <label class="status-p">
                            <input type="radio" name="status_<%= s.getId() %>" value="Present" checked> P
                        </label>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label class="status-a">
                            <input type="radio" name="status_<%= s.getId() %>" value="Absent"> A
                        </label>
                    </td>
                </tr>
            <% 
                    }
                } 
            %>
            </tbody>
        </table>

        <div style="text-align: center;">
            <button type="submit" class="btn-save">Save Attendance ✅</button>
        </div>
        
    </form>
    
    <br>
    <div style="text-align: center;"><a href="list">Back to Home</a></div>
</div>

</body>
</html>