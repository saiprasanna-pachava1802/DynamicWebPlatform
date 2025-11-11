package com.admin.servlet;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AdminAuthServlet")
public class AdminAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ca_students_feedback";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {

            if ("login".equals(action)) {
                String adminId = request.getParameter("admin_id").trim();
                String password = request.getParameter("password").trim();

                String sql = "SELECT * FROM admins_credentials WHERE admin_id = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, adminId);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("admin_id", adminId);
                    session.setAttribute("admin_logged_in", true);
                    response.sendRedirect("admin_dashboard.jsp");
                    return;
                } else {
                    request.setAttribute("loginError", "Invalid Admin ID or Password.");
                }

            } else if ("changePassword".equals(action)) {
                String adminId = request.getParameter("change_admin_id").trim();
                String oldPassword = request.getParameter("old_password").trim();
                String newPassword = request.getParameter("new_password").trim();
                String confirmPassword = request.getParameter("confirm_password").trim();

                if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("changeError", "New password and confirm password do not match.");
                } else if (newPassword.length() < 5) {
                    request.setAttribute("changeError", "New password must be at least 5 characters long.");
                } else if (!newPassword.matches(".*[A-Za-z].*")) {
                    request.setAttribute("changeError", "New password must contain at least one letter.");
                } else {
                    String checkSql = "SELECT * FROM admins_credentials WHERE admin_id = ? AND password = ?";
                    PreparedStatement stmt = conn.prepareStatement(checkSql);
                    stmt.setString(1, adminId);
                    stmt.setString(2, oldPassword);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String updateSql = "UPDATE admins_credentials SET password = ? WHERE admin_id = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, newPassword);
                        updateStmt.setString(2, adminId);

                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            request.setAttribute("changeSuccess", "Password updated successfully!");
                        } else {
                            request.setAttribute("changeError", "Error updating password.");
                        }
                    } else {
                        request.setAttribute("changeError", "Invalid Admin ID or Old Password.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("loginError", "Database error: " + e.getMessage());
        }

        RequestDispatcher rd = request.getRequestDispatcher("admin_login.jsp");
        rd.forward(request, response);
    }
}
