<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="register.css?v=1">
    <title>Student Registration</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="form-container">
    
        <h2>Register Form</h2>
        <form action="RegisterServlet" method="post">
            <label for="reg">Reg Number</label>
            <input type="text" name="reg" required>

            <label for="pass">Password</label>
            <input type="password" name="pass" required>

            <input type="submit" value="Register">
            <input type="button" value="Login Page" onclick="location.href='login.jsp';">
        </form>
        
    </div>
</body>
</html>
