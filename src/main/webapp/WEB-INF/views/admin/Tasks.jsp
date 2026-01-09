<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tasks - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp">
        <jsp:param name="active" value="tasks"/>
    </jsp:include>

    <div class="admin-content">
        <div class="admin-page-header">
            <h1><i class="fas fa-tasks"></i> Tasks Management</h1>
            <p>View and manage all tasks across users</p>
        </div>

        <c:if test="${not empty requestScope.error}">
            <div class="admin-alert error">
                <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
            </div>
        </c:if>

        <div class="admin-table-container">
            <div class="admin-table-header">
                <h2>All Tasks</h2>
            </div>
            
            <c:choose>
                <c:when test="${empty requestScope.tasks}">
                    <div class="admin-empty">
                        <i class="fas fa-tasks"></i>
                        <h3>No tasks found</h3>
                        <p>There are no tasks in the system yet.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Priority</th>
                                <th>Due Date</th>
                                <th>Status</th>
                                <th>User ID</th>
                                <th>Project</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="task" items="${requestScope.tasks}">
                                <tr>
                                    <td>${task.id}</td>
                                    <td>${task.title}</td>
                                    <td>
                                        <span class="priority-${task.priority}">
                                            <i class="fas fa-flag"></i> P${task.priority}
                                        </span>
                                    </td>
                                    <td>${task.dueDate}</td>
                                    <td>
                                        <span class="status-badge ${task.completedAt != null ? 'completed' : 'pending'}">
                                            ${task.completedAt != null ? 'Completed' : 'Pending'}
                                        </span>
                                    </td>
                                    <td>${task.userId}</td>
                                    <td>${task.projectId != null ? task.projectId : '-'}</td>
                                    <td>
                                        <div class="table-actions">
                                            <form action="${pageContext.request.contextPath}/admin/tasks/delete" method="post" style="display:inline;"
                                                  onsubmit="return confirm('Are you sure you want to delete this task?');">
                                                <input type="hidden" name="taskId" value="${task.id}">
                                                <button type="submit" class="table-btn delete" title="Delete Task">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>