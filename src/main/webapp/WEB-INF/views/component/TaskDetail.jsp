<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 12/20/2025
  Time: 9:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Task detail</title>
</head>
<body>
<h2>Task detail</h2>
<c:if test="${empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<c:if test="${not empty requestScope.task}">

    <form action="${pageContext.request.getContextPath}/tasks/update" method="post">
        <input type="hidden" name="id" value="${requestScope.task.id}">

        Title:<br>
        <input type="text" name="title" value="${requestScope.task.title}" required><br><br>

        Description:<br>
        <textarea name="description">${requestScope.task.description}</textarea><br><br>

        Priority:<br>
        <select name="priority">
            <option value="1" ${requestScope.task.priority == 1 ? 'selected' : ''}>Low</option>
            <option value="2" ${requestScope.task.priority == 2 ? 'selected' : ''}>Medium</option>
            <option value="3" ${requestScope.task.priority == 3 ? 'selected' : ''}>High</option>
        </select><br><br>

        Due Date:<br>
        <input type="date" name="dueDate" value="${requestScope.task.dueDate}"><br><br>

        Project ID:<br>
        <input type="number" name="projectId"
               value="${requestScope.task.projectIdObject}"><br><br>

        <button type="submit">Save</button>

    </form>
</c:if>
</body>
</html>