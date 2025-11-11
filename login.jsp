<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Student Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&family=Poppins:wght@600&display=swap" rel="stylesheet">
    <style>
        /* Reset & body */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Roboto', sans-serif;
            background: linear-gradient(135deg, #74ebd5 0%, #acb6e5 100%);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            display: flex;
            width: 900px;
            height: 500px;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }

        /* Left Side */
        .left {
            flex: 1;
            background: url('https://images.unsplash.com/photo-1581091870621-3b0c2e5f9c1d?crop=entropy&cs=tinysrgb&fit=crop&h=500&w=450') center/cover no-repeat;
            color: white;
            padding: 30px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            text-align: center;
            position: relative;
        }

        .left::after {
            content: '';
            position: absolute;
            top: 0; left: 0; right: 0; bottom: 0;
            background-color: rgba(0,0,0,0.5);
            z-index: 0;
        }

        .left h1, .engineers-day {
            position: relative;
            z-index: 1;
        }

        .left h1 {
            font-family: 'Poppins', sans-serif;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .engineers-day {
            background-color: rgba(255, 255, 255, 0.9);
            color: #004d40;
            padding: 15px 20px;
            border-radius: 10px;
            text-align: left;
            max-width: 320px;
            margin: auto;
            animation: slideUp 1s ease forwards;
            opacity: 0;
        }

        .engineers-day h3 {
            margin-bottom: 8px;
            font-size: 18px;
            font-weight: bold;
        }

        .engineers-day p {
            font-size: 14px;
            line-height: 1.4;
        }

        @keyframes slideUp {
            from { transform: translateY(20px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }

        /* Right Side */
        .right {
            flex: 1;
            background-color: #ffffff;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        .right h2 {
            color: #333;
            font-family: 'Poppins', sans-serif;
            font-size: 26px;
            margin-bottom: 10px;
        }

        .right p {
            color: #555;
            margin-bottom: 20px;
            font-size: 14px;
            text-align: center;
        }

        .right form {
            width: 100%;
            display: flex;
            flex-direction: column;
        }

        .right input[type="text"],
        .right input[type="password"] {
            padding: 12px 15px;
            margin: 10px 0;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .right input[type="text"]:focus,
        .right input[type="password"]:focus {
            border-color: #00796b;
            box-shadow: 0 0 8px rgba(0,121,107,0.3);
            outline: none;
        }

        .right input[type="submit"],
        .right input[type="button"] {
            padding: 12px;
            margin-top: 15px;
            border: none;
            border-radius: 8px;
            background-color: #00796b;
            color: white;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .right input[type="submit"]:hover,
        .right input[type="button"]:hover {
            background-color: #004d40;
        }

        .error-message {
            color: #d32f2f;
            font-size: 13px;
            margin-bottom: 10px;
        }

        /* Responsive */
        @media(max-width: 900px) {
            .container {
                flex-direction: column;
                width: 90%;
                height: auto;
            }
            .left, .right {
                flex: unset;
                width: 100%;
                padding: 30px;
            }
            .left {
                height: 250px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Left Side: Quote + Engineers' Day -->
        <div class="left">
            <h1>“Failure is the Stepping Stone to Success…”</h1>
            <div class="engineers-day">
                <h3>Happy Engineers' Day!</h3>
                <p>15th September marks Engineers' Day in India, honoring <strong>Sir M. Visvesvaraya</strong>. 
                   We celebrate innovation, problem-solving, and the engineering spirit that drives India forward.</p>
            </div>
        </div>

        <!-- Right Side: Login Form -->
        <div class="right">
            <h2>Student Login</h2>
            <p>Enter your details to sign in to your account</p>

            <% String error = (String) request.getAttribute("loginError");
               if (error != null) {
            %>
            <div class="error-message"><%= error %></div>
            <% } %>

            <form action="LoginServlet" method="post">
                <input type="text" name="reg" placeholder="Enter your username/email" required>
                <input type="password" name="pass" placeholder="Enter your password" required>
                <input type="submit" value="Login">
                <input type="button" value="Register" onclick="location.href='Register.jsp';">
            </form>
        </div>
    </div>
</body>
</html>
