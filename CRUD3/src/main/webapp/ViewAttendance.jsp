<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.crud.AttendanceDTO" %> <!DOCTYPE html>
<html>
<head>
<title>View Attendance Report</title>
<style>
    body { font-family: sans-serif; padding: 20px; background-color: #f4f4f4; }
    .report-card { background: white; padding: 20px; width: 60%; margin: auto; box-shadow: 0 0 10px rgba(0,0,0,0.1); border-radius: 8px; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
    th { background-color: #007bff; color: white; }
    .status-Present { color: green; font-weight: bold; }
    .status-Absent { color: red; font-weight: bold; background-color: #ffe6e6; }
</style>
</head>
<body>

<div class="report-card">
    <h2 style="text-align: center;">📅 Attendance Report</h2>
    
    <form action="view-attendance" method="get" style="text-align: center; margin-bottom: 20px;">
        <label>Select Date: </label>
        <input type="date" name="date" value="${selectedDate}" required style="padding: 5px;">
        <button type="submit" style="padding: 5px 15px; background: #333; color: white; border: none; cursor: pointer;">Show Report 🔍</button>
    </form>

    <hr>

    <%
        List<AttendanceDTO> list = (List<AttendanceDTO>) request.getAttribute("attendanceList");
        
        if (list != null && !list.isEmpty()) {
    %>
        <table>
            <thead>
                <tr>
                    <th>Roll No</th>
                    <th>Student Name</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <% for (AttendanceDTO d : list) { %>
                <tr>
                    <td><%= d.getRollNo() %></td>
                    <td><%= d.getName() %></td>
                    <td class="status-<%= d.getStatus() %>">
                        <%= d.getStatus() %>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
        
        <div style="margin-top: 15px; text-align: center;">
            <strong>Total Students: <%= list.size() %></strong>
        </div>

    <% } else if (request.getParameter("date") != null) { %>
        <h3 style="text-align: center; color: red;">No records found for this date! ❌</h3>
    <% } else { %>
        <p style="text-align: center; color: #666;">Select a date to view attendance.</p>
    <% } %>
    
    <div style="text-align: center; margin-top: 20px;">
        <a href="<%=request.getContextPath() %>/list" style="text-decoration: none; color: #007bff;">← Back to Home</a>
    </div>
</div>

</body>
</html>