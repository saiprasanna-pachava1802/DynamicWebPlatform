<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("admin_logged_in") == null) {
        response.sendRedirect("admin_login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
</head>
<body>
    <h2>Welcome, Admin <%= session.getAttribute("admin_id") %>!</h2>
    <p>You are successfully logged in to the administrator dashboard.</p>
    <a href="logout.jsp">Logout</a>
</body>
</html>
