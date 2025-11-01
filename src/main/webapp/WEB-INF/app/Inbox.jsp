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
<form method="get" action="${pageContext.request.contextPath}/app/inbox">
    <table class="task-table">
        <thead>
            <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Priority</th>
                <th>completed</th>
                <th>Due Date</th>
            </tr>
        </thead>
        <tbody>
            <jsp:useBean id="tasks" scope="request" type="java.util.List"/>
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>${task.priority}</td>
                    <td>${task.completed}</td>
                    <td>${task.dueDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
<%--    <jsp:include page="/WEB-INF/component/Add-task.jsp"/>--%>

</form>
</body>
</html>
