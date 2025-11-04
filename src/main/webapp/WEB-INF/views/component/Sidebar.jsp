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
        <li class="${param.activePage == 'inbox' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/app/inbox">
                Inbox
            </a>
        </li>
        <li class="${param.activePage == 'today' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/app/today">
                Today
            </a>
        </li>
        <li class="${param.activePage == 'upcoming' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/app/upcoming">
                Upcoming
            </a>
        </li>
        <li class="${param.activePage == 'completed' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/app/completed">
                completed
            </a>
        </li>
        <li class="${param.activePage == 'add-task' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/tasks/add-task">
                Add Task
            </a>
        </li>
        <li class="${param.activePage == 'projects' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/app/projects">
                Projects
            </a>
        </li>
        <li class="${param.activePage == 'logout' ? 'active' : ''}">
            <a href="${pageContext.request.contextPath}/auth/logout">
                Logout
            </a>
        </li>
    </ul>
</div>
</body>
</html>
