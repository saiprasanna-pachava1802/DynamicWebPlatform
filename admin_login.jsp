<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Authentication System</title>
</head>
<body>
    <h2>Admin Authentication System</h2>
    <hr>

    <!-- Login Form -->
    <h3>Admin Login</h3>
    <form method="post" action="AdminAuthServlet">
        <input type="hidden" name="action" value="login">
        <label>Admin ID:</label><br>
        <input type="text" name="admin_id" required><br><br>
        <label>Password:</label><br>
        <input type="password" name="password" required><br><br>
        <button type="submit">Login</button>
    </form>

    <% 
        String loginError = (String) request.getAttribute("loginError");
        if (loginError != null) { 
    %>
        <p style="color:red;"><%= loginError %></p>
    <% } %>

    <hr>

    <!-- Change Password Form -->
    <h3>Change Password</h3>
    <form method="post" action="AdminAuthServlet">
        <input type="hidden" name="action" value="changePassword">
        <label>Admin ID:</label><br>
        <input type="text" name="change_admin_id" required><br><br>
        <label>Old Password:</label><br>
        <input type="password" name="old_password" required><br><br>
        <label>New Password:</label><br>
        <input type="password" name="new_password" required><br><br>
        <label>Confirm Password:</label><br>
        <input type="password" name="confirm_password" required><br><br>
        <button type="submit">Update Password</button>
    </form>

    <%
        String changeError = (String) request.getAttribute("changeError");
        String changeSuccess = (String) request.getAttribute("changeSuccess");
        if (changeError != null) {
    %>
        <p style="color:red;"><%= changeError %></p>
    <% } else if (changeSuccess != null) { %>
        <p style="color:green;"><%= changeSuccess %></p>
    <% } %>
</body>
</html>
