<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.crud.Result" %>
<%@ page import="com.crud.Student" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Result</title>
<style>
    body { font-family: sans-serif; padding: 20px; background-color: #f4f4f4; }
    .container { background: white; padding: 20px; border-radius: 8px; width: 60%; margin: auto; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    table { width: 100%; border-collapse: collapse; margin-top: 15px; }
    th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
    th { background-color: #007bff; color: white; }
    input[type="number"] { width: 80px; padding: 5px; text-align: center; }
    .btn-save { background-color: #28a745; color: white; border: none; padding: 10px 20px; cursor: pointer; font-size: 16px; margin-top: 15px; }
</style>
</head>
<body>

<%
    // 1. Data Receive Karna
    Student student = (Student) request.getAttribute("student");
    Boolean isEditObj = (Boolean) request.getAttribute("isEdit");
    boolean isEdit = (isEditObj != null) ? isEditObj : false;
    
    // Lists nikalna (Cast karna padta hai scriptlets me)
    List<Result> existingResults = (List<Result>) request.getAttribute("results");
    String[] defaultSubjects = (String[]) request.getAttribute("subjects");
%>

<div class="container">
    <h2>Manage Result for: <%= (student != null) ? student.getName() : "Unknown" %></h2>
    <p>Roll No: <%= (student != null) ? student.getRoll_no() : "" %></p>

    <form action="save-result" method="post">
        <input type="hidden" name="studentId" value="<%= (student != null) ? student.getId() : "0" %>">
        <input type="hidden" name="isEdit" value="<%= isEdit %>">

        <table>
            <thead>
                <tr>
                    <th>Subject</th>
                    <th>Total Marks</th>
                    <th>Marks Obtained</th>
                </tr>
            </thead>
            <tbody>
            <% 
                // CASE 1: EDIT MODE (Purana data dikhana hai)
                if (isEdit && existingResults != null) {
                    for (int i = 0; i < existingResults.size(); i++) {
                        Result res = existingResults.get(i);
            %>
                    <tr>
                        <td>
                            <input type="text" name="subject_<%=i%>" value="<%= res.getSubject() %>" readonly style="border:none; text-align:center;">
                        </td>
                        <td>100</td>
                        <td>
                            <input type="number" name="marks_<%=i%>" value="<%= res.getMarks_obtained() %>" required min="0" max="100">
                            <input type="hidden" name="id_<%=i%>" value="<%= res.getId() %>">
                        </td>
                    </tr>
            <% 
                    } // Loop End
            %>
                    <input type="hidden" name="count" value="<%= existingResults.size() %>">
            <% 
                } else { 
                // CASE 2: INSERT MODE (Naye subjects dikhane hain)
                    if (defaultSubjects != null) {
                        for (int i = 0; i < defaultSubjects.length; i++) {
            %>
                    <tr>
                        <td>
                            <input type="text" name="subject_<%=i%>" value="<%= defaultSubjects[i] %>" readonly style="border:none; text-align:center;">
                        </td>
                        <td>100</td>
                        <td>
                            <input type="number" name="marks_<%=i%>" placeholder="00" required min="0" max="100">
                        </td>
                    </tr>
            <% 
                        } // Loop End
            %>
                    <input type="hidden" name="count" value="<%= defaultSubjects.length %>">
            <% 
                    }
                } 
            %>
            </tbody>
        </table>

        <div style="text-align: center;">
            <button type="submit" class="btn-save">Save Results 💾</button>
        </div>
    </form>
    
    <br>
    <div style="text-align: center;">
        <a href="list">Cancel</a>
    </div>
</div>

</body>
</html>