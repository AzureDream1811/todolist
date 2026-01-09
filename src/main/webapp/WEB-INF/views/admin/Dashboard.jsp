<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp">
        <jsp:param name="active" value="dashboard"/>
    </jsp:include>

    <div class="admin-content">
        <div class="admin-page-header">
            <h1><i class="fas fa-tachometer-alt"></i> Dashboard</h1>
            <p>Welcome back, <strong>${sessionScope.currentUser.username}</strong>! Here's an overview of your application.</p>
        </div>

        <c:if test="${not empty requestScope.error}">
            <div class="admin-alert error">
                <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
            </div>
        </c:if>

        <!-- Stats Cards -->
        <div class="admin-stats">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon users">
                        <i class="fas fa-users"></i>
                    </div>
                </div>
                <div class="stat-card-value">${totalUsers}</div>
                <div class="stat-card-label">Total Users</div>
            </div>

            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon tasks">
                        <i class="fas fa-tasks"></i>
                    </div>
                </div>
                <div class="stat-card-value">${totalTasks}</div>
                <div class="stat-card-label">Total Tasks</div>
            </div>

            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon projects">
                        <i class="fas fa-folder"></i>
                    </div>
                </div>
                <div class="stat-card-value">${totalProjects}</div>
                <div class="stat-card-label">Total Projects</div>
            </div>

            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon completed">
                        <i class="fas fa-check-circle"></i>
                    </div>
                </div>
                <div class="stat-card-value">${completedTasks}</div>
                <div class="stat-card-label">Completed Tasks</div>
            </div>
        </div>

        <!-- Quick Actions & Recent Activity -->
        <div class="dashboard-grid">
            <!-- Quick Actions -->
            <div class="dashboard-card">
                <div class="dashboard-card-header">
                    <h3><i class="fas fa-bolt"></i> Quick Actions</h3>
                </div>
                <div class="dashboard-card-body">
                    <div class="quick-actions">
                        <a href="${pageContext.request.contextPath}/admin/users" class="quick-action-btn">
                            <i class="fas fa-users"></i>
                            <span>Manage Users</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/tasks" class="quick-action-btn">
                            <i class="fas fa-tasks"></i>
                            <span>View All Tasks</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/projects" class="quick-action-btn">
                            <i class="fas fa-folder"></i>
                            <span>View All Projects</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/app/inbox" class="quick-action-btn highlight">
                            <i class="fas fa-home"></i>
                            <span>Go to My App</span>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Recent Users -->
            <div class="dashboard-card">
                <div class="dashboard-card-header">
                    <h3><i class="fas fa-user-plus"></i> Recent Users</h3>
                    <a href="${pageContext.request.contextPath}/admin/users" class="view-all-link">View All</a>
                </div>
                <div class="dashboard-card-body">
                    <c:choose>
                        <c:when test="${empty recentUsers}">
                            <div class="empty-state-small">
                                <p>No users yet</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="recent-list">
                                <c:forEach var="user" items="${recentUsers}">
                                    <div class="recent-item">
                                        <div class="recent-avatar">
                                            ${user.username.substring(0,1).toUpperCase()}
                                        </div>
                                        <div class="recent-info">
                                            <div class="recent-name">${user.username}</div>
                                            <div class="recent-meta">${user.email}</div>
                                        </div>
                                        <span class="role-badge ${user.role eq 'ADMIN' ? 'admin' : 'user'}">
                                            ${user.role}
                                        </span>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Recent Tasks -->
            <div class="dashboard-card">
                <div class="dashboard-card-header">
                    <h3><i class="fas fa-clock"></i> Recent Tasks</h3>
                    <a href="${pageContext.request.contextPath}/admin/tasks" class="view-all-link">View All</a>
                </div>
                <div class="dashboard-card-body">
                    <c:choose>
                        <c:when test="${empty recentTasks}">
                            <div class="empty-state-small">
                                <p>No tasks yet</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="recent-list">
                                <c:forEach var="task" items="${recentTasks}">
                                    <div class="recent-item">
                                        <div class="task-status-icon ${task.completedAt != null ? 'completed' : ''}">
                                            <i class="fas ${task.completedAt != null ? 'fa-check-circle' : 'fa-circle'}"></i>
                                        </div>
                                        <div class="recent-info">
                                            <div class="recent-name">${task.title}</div>
                                            <div class="recent-meta">
                                                <c:if test="${not empty task.dueDate}">
                                                    Due: ${task.dueDate}
                                                </c:if>
                                            </div>
                                        </div>
                                        <span class="priority-badge priority-${task.priority}">
                                            P${task.priority}
                                        </span>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- System Info -->
            <div class="dashboard-card">
                <div class="dashboard-card-header">
                    <h3><i class="fas fa-info-circle"></i> System Info</h3>
                </div>
                <div class="dashboard-card-body">
                    <div class="system-info-list">
                        <div class="system-info-item">
                            <span class="info-label">Application</span>
                            <span class="info-value">TodoList App</span>
                        </div>
                        <div class="system-info-item">
                            <span class="info-label">Version</span>
                            <span class="info-value">1.0.0</span>
                        </div>
                        <div class="system-info-item">
                            <span class="info-label">Java Version</span>
                            <span class="info-value"><%= System.getProperty("java.version") %></span>
                        </div>
                        <div class="system-info-item">
                            <span class="info-label">Server</span>
                            <span class="info-value"><%= application.getServerInfo() %></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
