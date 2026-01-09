<%--
  Created by IntelliJ IDEA.
  User: Hi
  Date: 05/11/2025
  Time: 10:52 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.LocalDate" %>

<html>
<head>
    <title>Completed Tasks - Todoist</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/completed.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="app-container">
    <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
        <jsp:param name="active" value="completed"/>
    </jsp:include>

    <main class="main-content">
        <div class="content-wrapper">
            <div class="completed-container">

                <header class="activity-header">
                    <h2>Activity: All projects</h2>
                    <span class="total-counter">Completed tasks (${totalCompleted})</span>
                </header>

                <c:choose>
                    <c:when test="${empty groupedTasks}">
                        <div class="empty-state" style="text-align: center; padding-top: 100px;">
                            <i class="fas fa-check-circle" style="font-size: 80px; color: #4caf50; opacity: 0.4;"></i>
                            <p style="color: #888; margin-top: 15px;">No completed tasks yet.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%-- Duyệt qua từng nhóm ngày từ Servlet --%>
                        <c:forEach var="entry" items="${groupedTasks}">
                            <div class="date-group-header">
                                <c:set var="groupDate" value="${entry.key}"/>
                                <c:choose>
                                    <c:when test="${groupDate == LocalDate.now()}">Today</c:when>
                                    <c:when test="${groupDate == LocalDate.now().minusDays(1)}">Yesterday</c:when>
                                    <c:otherwise>${groupDate}</c:otherwise>
                                </c:choose>
                            </div>

                            <%-- Duyệt các task trong ngày đó --%>
                            <c:forEach var="task" items="${entry.value}">
                                <div class="completed-task-item">
                                    <div class="check-icon-wrapper">
                                        <i class="fa-solid fa-check-circle"></i>
                                    </div>

                                    <div class="task-main-info">
                                        <div class="action-text">
                                            <span class="user-name">You</span>
                                            <span class="action-desc">completed a task:</span>
                                            <span class="completed-task-title">${task.title}</span>
                                        </div>
                                        <div class="completed-time">${task.completedAt}</div>
                                    </div>

                                    <div class="project-info">
                                        <span class="project-name">
                                                ${task.projectIdObject == null ? 'Inbox' : 'Project'}
                                        </span>
                                        <i class="fa-solid fa-inbox" style="font-size: 10px; opacity: 0.6;"></i>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </main>
</div>
</body>
</html>