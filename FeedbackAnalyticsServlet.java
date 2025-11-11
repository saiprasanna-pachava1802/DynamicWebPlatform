package com.admin.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/FeedbackAnalyticsServlet")
public class FeedbackAnalyticsServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ca_students_feedback";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
            List<Map<String, Object>> forms = new ArrayList<>();

            // Fetch forms
            String formsSql = "SELECT id, title, created_at FROM forms ORDER BY created_at DESC";
            Statement stmt = conn.createStatement();
            ResultSet rsForms = stmt.executeQuery(formsSql);
            while (rsForms.next()) {
                Map<String, Object> form = new HashMap<>();
                form.put("id", rsForms.getInt("id"));
                form.put("title", rsForms.getString("title"));
                form.put("created_at", rsForms.getTimestamp("created_at"));
                forms.add(form);
            }

            int selectedFormId = 0;
            String selectedSection = "All";
            if (request.getParameter("form_id") != null)
                selectedFormId = Integer.parseInt(request.getParameter("form_id"));
            if (request.getParameter("section") != null)
                selectedSection = request.getParameter("section");

            // Prepare default data
            List<String> sections = new ArrayList<>();
            int totalStudents = 0, respondedStudents = 0;
            double avgRating = 0.0;
            List<Map<String, Object>> subjects = new ArrayList<>();
            List<String> subjectNames = new ArrayList<>();
            List<Double> subjectAvgRatings = new ArrayList<>();
            Map<String, List<Integer>> subjectRatingsData = new HashMap<>();

            if (selectedFormId > 0) {
                // Get branch, year
                PreparedStatement psForm = conn.prepareStatement(
                        "SELECT title, branch, year FROM forms WHERE id = ?");
                psForm.setInt(1, selectedFormId);
                ResultSet rsForm = psForm.executeQuery();

                String branch = "", year = "", formTitle = "";
                if (rsForm.next()) {
                    formTitle = rsForm.getString("title");
                    branch = rsForm.getString("branch");
                    year = rsForm.getString("year");
                }
                request.setAttribute("currentFormTitle", formTitle);

                // Sections
                PreparedStatement psSections = conn.prepareStatement(
                        "SELECT DISTINCT sd.section FROM student_details sd " +
                        "JOIN feedback f ON sd.reg_number = f.reg_number " +
                        "WHERE sd.branch = ? AND sd.year = ? AND f.form_id = ? ORDER BY sd.section ASC");
                psSections.setString(1, branch);
                psSections.setString(2, year);
                psSections.setInt(3, selectedFormId);
                ResultSet rsSections = psSections.executeQuery();
                while (rsSections.next()) {
                    sections.add(rsSections.getString("section"));
                }

                // Subject codes
                PreparedStatement psSubCodes = conn.prepareStatement(
                        "SELECT subject_code FROM form_subjects WHERE form_id = ?");
                psSubCodes.setInt(1, selectedFormId);
                ResultSet rsSubCodes = psSubCodes.executeQuery();
                List<String> subjectCodes = new ArrayList<>();
                while (rsSubCodes.next()) subjectCodes.add(rsSubCodes.getString("subject_code"));

                if (!subjectCodes.isEmpty()) {
                    String inClause = String.join("','", subjectCodes);

                    // Total students
                    String totalSql = "SELECT COUNT(*) AS count FROM student_details WHERE branch=? AND year=?";
                    if (!selectedSection.equals("All")) totalSql += " AND section=?";
                    PreparedStatement psTotal = conn.prepareStatement(totalSql);
                    psTotal.setString(1, branch);
                    psTotal.setString(2, year);
                    if (!selectedSection.equals("All")) psTotal.setString(3, selectedSection);
                    ResultSet rsTotal = psTotal.executeQuery();
                    if (rsTotal.next()) totalStudents = rsTotal.getInt("count");

                    // Responded students
                    String respSql = "SELECT COUNT(DISTINCT f.reg_number) AS count FROM feedback f " +
                            "JOIN student_details sd ON f.reg_number = sd.reg_number " +
                            "WHERE sd.branch=? AND sd.year=? AND f.subject_code IN ('" + inClause + "')";
                    if (!selectedSection.equals("All")) respSql += " AND sd.section=?";
                    PreparedStatement psResp = conn.prepareStatement(respSql);
                    psResp.setString(1, branch);
                    psResp.setString(2, year);
                    if (!selectedSection.equals("All")) psResp.setString(3, selectedSection);
                    ResultSet rsResp = psResp.executeQuery();
                    if (rsResp.next()) respondedStudents = rsResp.getInt("count");

                    // Avg rating
                    String avgSql = "SELECT AVG(f.rating) AS avg_rating FROM feedback f " +
                            "JOIN student_details sd ON f.reg_number = sd.reg_number " +
                            "WHERE sd.branch=? AND sd.year=? AND f.subject_code IN ('" + inClause + "')";
                    if (!selectedSection.equals("All")) avgSql += " AND sd.section=?";
                    PreparedStatement psAvg = conn.prepareStatement(avgSql);
                    psAvg.setString(1, branch);
                    psAvg.setString(2, year);
                    if (!selectedSection.equals("All")) psAvg.setString(3, selectedSection);
                    ResultSet rsAvg = psAvg.executeQuery();
                    if (rsAvg.next()) avgRating = rsAvg.getDouble("avg_rating");

                    // Subjects
                    PreparedStatement psSubjects = conn.prepareStatement(
                            "SELECT s.subject_code, s.subject_name FROM subjects s " +
                                    "JOIN form_subjects fs ON s.subject_code=fs.subject_code WHERE fs.form_id=?");
                    psSubjects.setInt(1, selectedFormId);
                    ResultSet rsSubjects = psSubjects.executeQuery();
                    while (rsSubjects.next()) {
                        Map<String, Object> subject = new HashMap<>();
                        subject.put("subject_code", rsSubjects.getString("subject_code"));
                        subject.put("subject_name", rsSubjects.getString("subject_name"));
                        subjects.add(subject);
                    }

                    // Subject averages
                    PreparedStatement psSubAvg = conn.prepareStatement(
                            "SELECT s.subject_name, AVG(f.rating) AS avg_rating FROM feedback f " +
                                    "JOIN subjects s ON f.subject_code=s.subject_code " +
                                    "JOIN student_details sd ON f.reg_number=sd.reg_number " +
                                    "WHERE sd.branch=? AND sd.year=? AND f.subject_code IN ('" + inClause + "') " +
                                    "GROUP BY f.subject_code, s.subject_name ORDER BY avg_rating DESC");
                    psSubAvg.setString(1, branch);
                    psSubAvg.setString(2, year);
                    ResultSet rsSubAvg = psSubAvg.executeQuery();
                    while (rsSubAvg.next()) {
                        subjectNames.add(rsSubAvg.getString("subject_name"));
                        subjectAvgRatings.add(rsSubAvg.getDouble("avg_rating"));
                    }

                    // Subject ratings
                    for (Map<String, Object> subj : subjects) {
                        String subjectCode = subj.get("subject_code").toString();
                        List<Integer> ratings = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
                        PreparedStatement psRatings = conn.prepareStatement(
                                "SELECT rating, COUNT(DISTINCT f.reg_number) AS count FROM feedback f " +
                                        "JOIN student_details sd ON f.reg_number=sd.reg_number " +
                                        "WHERE sd.branch=? AND sd.year=? AND f.subject_code=? " +
                                        "GROUP BY rating");
                        psRatings.setString(1, branch);
                        psRatings.setString(2, year);
                        psRatings.setString(3, subjectCode);
                        ResultSet rsRatings = psRatings.executeQuery();
                        while (rsRatings.next()) {
                            int rating = rsRatings.getInt("rating");
                            ratings.set(rating - 1, rsRatings.getInt("count"));
                        }
                        subjectRatingsData.put(subjectCode, ratings);
                    }
                }
            }

            request.setAttribute("forms", forms);
            request.setAttribute("selectedFormId", selectedFormId);
            request.setAttribute("selectedSection", selectedSection);
            request.setAttribute("sections", sections);
            request.setAttribute("totalStudents", totalStudents);
            request.setAttribute("respondedStudents", respondedStudents);
            request.setAttribute("avgRating", avgRating);
            request.setAttribute("subjects", subjects);
            request.setAttribute("subjectNames", subjectNames);
            request.setAttribute("subjectAvgRatings", subjectAvgRatings);
            request.setAttribute("subjectRatingsData", subjectRatingsData);

            RequestDispatcher rd = request.getRequestDispatcher("feedback_analytics.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
