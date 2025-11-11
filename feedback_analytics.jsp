<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Feedback Analytics Dashboard</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0"></script>
</head>
<body>

<h1>Feedback Analytics Dashboard</h1>

<form action="FeedbackAnalyticsServlet" method="get">
  <label>Select Form:</label>
  <select name="form_id" onchange="this.form.submit()">
    <% List<Map<String, Object>> forms = (List<Map<String, Object>>) request.getAttribute("forms");
       Integer selectedFormId = (Integer) request.getAttribute("selectedFormId");
       for (Map<String, Object> form : forms) {
           int id = (Integer) form.get("id");
           String title = (String) form.get("title");
    %>
      <option value="<%= id %>" <%= (id == selectedFormId ? "selected" : "") %>>
        <%= title %>
      </option>
    <% } %>
  </select>
</form>

<% Integer total = (Integer) request.getAttribute("totalStudents");
   Integer responded = (Integer) request.getAttribute("respondedStudents");
   Double avgRating = (Double) request.getAttribute("avgRating");
%>

<h3>Total Students: <%= total %></h3>
<h3>Responded: <%= responded %></h3>
<h3>Average Rating: <%= String.format("%.2f", avgRating) %></h3>

<canvas id="subjectPerformanceChart" width="600" height="400"></canvas>

<script>
  const ctx = document.getElementById('subjectPerformanceChart').getContext('2d');
  const labels = <%= new org.json.JSONArray((List<String>)request.getAttribute("subjectNames")) %>;
  const data = <%= new org.json.JSONArray((List<Double>)request.getAttribute("subjectAvgRatings")) %>;

  new Chart(ctx, {
    type: 'bar',
    data: { labels: labels, datasets: [{ label: 'Average Rating', data: data }] },
    options: { scales: { y: { beginAtZero: true, max: 5 } } }
  });
</script>

</body>
</html>
