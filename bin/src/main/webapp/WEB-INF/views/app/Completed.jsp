<%--
  Created by IntelliJ IDEA.
  User: Hi
  Date: 05/11/2025
  Time: 10:52 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Completed Tasks</title>
</head>
<body>
<!-- add sidebar-->
<jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
    <jsp:param name="active" value="completed"/>
</jsp:include>

<h1>Completed Tasks</h1>

<%-- error --%>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<!-- Completed Tasks Section -->
<h2>Completed Tasks (${CompletedTasks.size()})</h2>
<c:choose>
    <c:when test="${empty CompletedTasks}">
        <p>No completed tasks!</p>
    </c:when>
    <c:otherwise>
        <table border="1" style="width: 50%; border-collapse: collapse;">
            <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Status</th>
            </tr>
            <c:forEach var="task" items="${CompletedTasks}">
                <tr>
                    <td>
                        <h3>${task.title}</h3>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty task.description}">
                                <p>${task.description}</p>
                            </c:when>
                            <c:otherwise>
                                <p>No description</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <span style="color: green; font-weight: bold;">COMPLETED</span>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<br>
</body>
</html>