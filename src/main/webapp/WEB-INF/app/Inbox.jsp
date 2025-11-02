<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/1/2025
  Time: 11:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Inbox</title>
</head>
<body>
<h2>Inbox</h2>
<%-- error --%>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<table>
    <thead>
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Priority</th>
        <th>Completed</th>
        <th>Due Date</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="task" items="${requestScope.tasks}">
        <tr>
            <td>${task.title}</td>
            <td>${task.description}</td>
            <td>${task.priority}</td>
            <td>${task.completed ? 'Yes' : 'No'}</td>
            <td>${task.dueDate}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<!-- Add Task Form -->
<jsp:include page="/WEB-INF/component/AddTask.jsp"/>
</body>
</html>
