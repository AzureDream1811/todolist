<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 12/17/2025
  Time: 4:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Tasks</title>
</head>
<body>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>
<jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp" />

<div style="margin-left:220px;">
<h2>All Tasks</h2>
<c:choose>
    <c:when test="${empty requestScope.tasks}">
        <h2>No tasks</h2>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>id</th>
                <th>title</th>
                <th>description</th>
                <th>priority</th>
                <th>dueDate</th>
                <th>completedAt</th>
                <th>projectId</th>
                <th>userId</th>
                <th>createdAt</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="task" items="${requestScope.tasks}">
                <tr>
                    <td>${task.id}</td>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>${task.priority}</td>
                    <td>${task.dueDate}</td>
                    <td>${task.completedAt}</td>
                    <td>${task.projectId}</td>
                    <td>${task.userId}</td>
                    <td>${task.createdAt}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/admin/tasks/delete" method="post">
                            <input type="hidden" name="taskId" value="${task.id}">
                            <button type="submit">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
</c:otherwise>
</c:choose>
</div>
</body>
</html>