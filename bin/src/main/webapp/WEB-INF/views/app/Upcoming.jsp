<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/6/2025
  Time: 7:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<html>
<head>
    <title>Upcoming</title>
</head>
<body>
<!-- sidebar -->
<jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
    <jsp:param name="active" value="upcoming"/>
</jsp:include>

<!-- main -->
<h1>Upcoming Tasks</h1>

<!-- error -->
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<c:choose>
    <c:when test="${empty requestScope.upcomingTasks}">
        No upcoming task !!
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Due Date</th>
                <th>Priority</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="task" items="${requestScope.upcomingTasks}">
                <tr>
                    <!-- TẤT CẢ TRONG 1 HÀNG -->
                    <td>${task.id}</td>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>${task.dueDate}</td>
                    <td>${task.priority}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<%-- Include AddTask component với tham số taskType --%>
<jsp:include page="../component/AddTask.jsp">
    <jsp:param name="taskType" value="upcoming"/>
</jsp:include>

<jsp:include page="../component/DeleteTask.jsp">
    <jsp:param name="taskType" value="upcoming"/>
</jsp:include>

</body>
</html>
