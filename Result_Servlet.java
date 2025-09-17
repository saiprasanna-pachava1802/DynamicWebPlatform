package p1;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class Result_Servlet extends HttpServlet {

    private static final String[] CORRECT_ANSWERS = {
        "D) All the above",
        "B) Chiranjeevi",
        "A) China",
        "C) MS Dhoni",
        "B) David Warner"
    };

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("answers") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        String reg = (String) session.getAttribute("regNumber");

        int score = 0;
        for (int i = 0; i < CORRECT_ANSWERS.length; i++) {
            String userAns = answers.getOrDefault(i, "").replaceAll("\\s+", "").toUpperCase();
            String correctAns = CORRECT_ANSWERS[i].replaceAll("\\s+", "").toUpperCase();

            if (userAns.equals(correctAns)) {
                score++;
            }
        }

        // Save to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:1818/app_exam", "root", "root1818");

            PreparedStatement ps = con.prepareStatement("INSERT INTO results (reg_number, score) VALUES (?, ?)");
            ps.setString(1, reg);
            ps.setInt(2, score);
            ps.executeUpdate();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.invalidate();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><style>");
        out.println("body { font-family: Arial; text-align: center; background-color: #f5f5f5; padding-top: 50px; }");
        out.println("h2 { color: green; font-size: 24px; }");
        out.println("p { font-size: 18px; color: #333; }");
        out.println("</style></head><body>");
        out.println("<h2>Thank you, successfully submitted!</h2>");
        out.println("<p>Your Score: " + score + " / " + CORRECT_ANSWERS.length + "</p>");
        out.println("<a href=\"Index.html\" style=\"display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; font-size: 16px;\">Home</a>");
        out.println("</body></html>");
        out.flush();
        out.close();
    }
}
