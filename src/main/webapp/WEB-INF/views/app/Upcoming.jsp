<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/6/2025
  Time: 7:17 AM
  To change this template use File | Settings | File Templates.
--%>
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
            <c:forEach var="task" items="${requestScope.upcomingTasks}">
                <tr>
                    <td>${task.title}</td>
                </tr>
                <tr>
                    <td>${task.description}</td>

                </tr>
                <tr>
                    <td>${task.dueDate}</td>
                    <td>${task.priority}</td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>
