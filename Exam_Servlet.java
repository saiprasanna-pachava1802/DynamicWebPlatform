package p1;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
public class Exam_Servlet extends HttpServlet {

	private static final String[][] questions = {
		    {
		        "When is Engineers’ Day celebrated in India?",
		        "A) 15th September",
		        "B) 15th October",
		        "C) 26th January",
		        "D) 5th September"
		    },
		    {
		        "Who is known as the 'Father of Indian Engineering'?",
		        "A) Sir M. Visvesvaraya",
		        "B) C. V. Raman",
		        "C) Homi Bhabha",
		        "D) Vikram Sarabhai"
		    },
		    {
		        "Which of the following Indian scientists won the Nobel Prize in Physics or contributed to major discoveries?",
		        "A) C. V. Raman",
		        "B) Homi Bhabha",
		        "C) Jagadish Chandra Bose",
		        "D) A. P. J. Abdul Kalam",
		        "E) Satyendra Nath Bose"
		    },
		    {
		        "Which of the following is a renewable source of energy widely used in India?",
		        "A) Solar Energy",
		        "B) Coal",
		        "C) Natural Gas",
		        "D) Nuclear Energy"
		    },
		    {
		        "Which Indian engineers/scientists have also held prominent leadership or president positions in India?",
		        "A) R. K. Pachauri",
		        "B) A. P. J. Abdul Kalam",
		        "C) Vikram Sarabhai",
		        "D) Satish Dhawan",
		        "E) M. Visvesvaraya"
		    }
		};

		// For checkbox questions, we can store correct answers as comma-separated values
		private static final String[] correctAnswers = {
		    "A) 15th September",
		    "A) Sir M. Visvesvaraya",
		    "A) C. V. Raman,E) Satyendra Nath Bose", // Question 3 - 2 correct options
		    "A) Solar Energy",
		    "B) A. P. J. Abdul Kalam,E) M. Visvesvaraya" // Question 5 - 2 correct options
		};


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showQuestionPage(request, response, 0);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String regNumber = (String) session.getAttribute("regNumber");

        Map<Integer, String> answerMap = (Map<Integer, String>) session.getAttribute("answers");
        if (answerMap == null) {
            answerMap = new HashMap<>();
        }

        int qno = Integer.parseInt(request.getParameter("qno"));
        String action = request.getParameter("action");

        String answer = request.getParameter("answer");
        if (answer != null) {
            answerMap.put(qno, answer);
            session.setAttribute("answers", answerMap);
        }

        if ("next".equals(action)) {
            qno++;
            showQuestionPage(request, response, qno);
        } else if ("back".equals(action)) {
            qno--;
            showQuestionPage(request, response, qno);
        } else if ("submit".equals(action)) {
            int score = 0;

            for (int i = 0; i < correctAnswers.length; i++) {
                String userAns = answerMap.getOrDefault(i, "").replaceAll("\\s+", "").toUpperCase();
                String correctAns = correctAnswers[i].replaceAll("\\s+", "").toUpperCase();

                if (userAns.equals(correctAns)) {
                    score++;
                }
            }

            // Store result in DB
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:1818/app_exam", "root", "root1818");

                PreparedStatement ps = con.prepareStatement("INSERT INTO results (reg_number, score) VALUES (?, ?)");
                ps.setString(1, regNumber);
                ps.setInt(2, score);
                ps.executeUpdate();

                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Show final message
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body style='background:#f0fff0; text-align:center; padding-top:100px;'>");
            out.println("<h2 style='color:green;'>Thank you, successfully submitted!</h2>");
            out.println("<h3>Your Score: " + score + " out of " + correctAnswers.length + "</h3>");
            out.println("<a href=\"Index.html\" style=\"display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; font-size: 16px;\">Home</a>");
            out.println("</body></html>");
        }
    }

    private void showQuestionPage(HttpServletRequest request, HttpServletResponse response, int qno)
            throws ServletException, IOException {

        String[] qdata = questions[qno];
        String question = qdata[0];
        String[] options = Arrays.copyOfRange(qdata, 1, qdata.length);

        request.setAttribute("qno", qno);
        request.setAttribute("question", question);
        request.setAttribute("options", options);
        request.setAttribute("isLast", (qno == questions.length - 1));

        RequestDispatcher rd = request.getRequestDispatcher("exam.jsp");
        rd.forward(request, response);
    }
}
