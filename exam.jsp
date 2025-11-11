<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
    String regNumber = (String) session.getAttribute("regNumber");
    Integer qno = (Integer) request.getAttribute("qno");
    String question = (String) request.getAttribute("question");
    String[] options = (String[]) request.getAttribute("options");
    boolean isLast = (Boolean) request.getAttribute("isLast");
%>

<html>
<head>
    <title>Online Exam</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f0f4f8;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            min-height: 100vh;
            padding-top: 50px;
        }
        .container {
            background-color: white;
            padding: 30px 40px;
            border-radius: 12px;
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
            width: 600px;
        }
        .question-header {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .option {
            margin-bottom: 10px;
            font-size: 16px;
        }
        h3 {
            margin-bottom: 25px;
            color: #333;
        }
        .btns {
            margin-top: 25px;
            display: flex;
            justify-content: space-between;
        }
        button {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            background-color: #007bff;
            color: white;
            cursor: pointer;
            font-size: 15px;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <h3>Welcome, Reg No: <%= regNumber %></h3>
    <form action="ExamServlet" method="post">
        <input type="hidden" name="qno" value="<%= qno %>">

        <div class="question-header">
            Q<%= qno + 1 %>: <%= question %>
        </div>

        <% for (int i = 0; i < options.length; i++) { %>
            <div class="option">
                <label>
                    <input type="<%= (qno == 2 || qno == 4) ? "checkbox" : "radio" %>"
                           name="answer"
                           value="<%= options[i] %>">
                    <%= options[i] %>
                </label>
            </div>
        <% } %>

        <div class="btns">
            <% if (qno > 0) { %>
                <button type="submit" name="action" value="back">Back</button>
            <% } else { %>
                <span></span>
            <% } %>

            <% if (!isLast) { %>
                <button type="submit" name="action" value="next">Next</button>
            <% } else { %>
                <button type="submit" name="action" value="submit">Submit</button>
            <% } %>
        </div>
    </form>
</div>
</body>
</html>