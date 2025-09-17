package p1;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Login_Servlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String regNumber = request.getParameter("reg");
        String password = request.getParameter("pass");

        if (regNumber == null || password == null || regNumber.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("loginError", "Registration number or password cannot be empty.");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:1818/app_exam", "root", "root1818");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM app_exam_mca25 WHERE reg_number = ? AND password = ?");
            ps.setString(1, regNumber);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("regNumber", regNumber);
                response.sendRedirect("ExamServlet");
            } else {
                // Send error to be shown inside the login box
                request.setAttribute("loginError", "Invalid Registration Number or Password");
                RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                rd.forward(request, response);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("loginError", "Server Error: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }
    }
}
