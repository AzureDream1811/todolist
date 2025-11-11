<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/4/2025
  Time: 12:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<html>
<body>
<div>
    <h2>${sessionScope.currentUser.username}</h2>
    <ul>
        <li>
            <a href="${pageContext.request.contextPath}/app/inbox">
                Inbox
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/app/today">
                Today
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/app/upcoming">
                Upcoming
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/app/completed">
                Completed
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/app/tasks">
                Add Task
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/app/projects">
                Projects
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/auth/logout">
                Logout
            </a>
        </li>
    </ul>
</div>
</body>
</html>
