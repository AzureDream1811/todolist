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
    <title>Today's Tasks</title>
</head>
<body>
<!-- add sidebar-->
<jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
    <jsp:param name="active" value="today"/>
</jsp:include>

<h1>Today's Overview</h1>

<%-- error --%>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<!-- Overdue Tasks Section -->
<h2>Overdue Tasks (${overdueTasks.size()})</h2>
<c:choose>
    <c:when test="${empty overdueTasks}">
        <p>No overdue tasks!</p>
    </c:when>
    <c:otherwise>
        <table>
            <c:forEach var="task" items="${overdueTasks}">
                <tr>
                    <td>
                        <h3>${task.title}</h3>
                        <c:if test="${not empty task.description}">
                            <p>${task.description}</p>
                        </c:if>
                        <p>
                            Due: ${task.dueDate} <!-- ĐÃ SỬA -->
                            | Priority: ${task.priority}
                            | <span style="color: red">OVERDUE</span>
                        </p>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<!-- Today's Tasks Section -->
<h2>Today's Tasks (${todayTasks.size()})</h2>
<c:choose>
    <c:when test="${empty todayTasks}">
        <p>No tasks for today!</p>
    </c:when>
    <c:otherwise>
        <table>
            <c:forEach var="task" items="${todayTasks}">
                <tr>
                    <td>
                        <h3>${task.title}</h3>
                        <c:if test="${not empty task.description}">
                            <p>${task.description}</p>
                        </c:if>
                        <p>
                            Due: <span style="color: green">Today!</span>
                            | Priority: ${task.priority}
                        </p>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<%-- Include AddTask component với tham số taskType --%>
<jsp:include page="../component/AddTask.jsp">
    <jsp:param name="taskType" value="today"/>
</jsp:include>

</body>
</html>