package p1;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Register_Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reg = request.getParameter("reg");
        String pass = request.getParameter("pass");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:1818/app_exam", "root", "root1818");
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO app_exam_mca25 (Reg_number, password) VALUES (?, ?)");
            ps.setString(1, reg);
            ps.setString(2, pass);
            int i = ps.executeUpdate();
            if (i > 0) {
                out.println("<html><body>");
                out.println("<h2 style='text-align:center;'>Successfully Registered.</h2>");
                out.println("<div style='text-align:center;'>");
                out.println("<form action='login.jsp'>");
                out.println("<input type='submit' value='Click To Login_Page'>");
                out.println("</form></div></body></html>");
            }
            con.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            out.println("<h3 style='color:red;'> Reg_Number already exists!</h3>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
