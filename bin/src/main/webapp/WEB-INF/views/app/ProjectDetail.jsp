<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
  <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
    <jsp:param name="active" value="projects"/>
  </jsp:include>

  <h2>Project: ${requestScope.project.name}</h2>

  <c:if test="${empty requestScope.projectTasks}">
    <p>No tasks in this project.</p>
  </c:if>

  <c:if test="${not empty requestScope.projectTasks}">
    <table border="1" cellpadding="6">
      <tr>
        <th>Title</th>
        <th>Due</th>
        <th>Priority</th>
        <th>Done?</th>
        <th>Created</th>
      </tr>
      <c:forEach var="t" items="${requestScope.projectTasks}">
        <tr>
          <td>${t.title}</td>
          <td><c:out value='${t.dueDate}'/></td>
          <td>${t.priority}</td>
          <td>${t.completedAt != null ? 'Yes' : 'No'}</td>
          <td>${t.createdAt}</td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</body>
</html>
