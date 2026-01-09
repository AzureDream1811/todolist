<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${requestScope.project.name} - Project</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/projectDetail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="app-container">
    <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
        <jsp:param name="active" value="projects"/>
    </jsp:include>

    <main class="main-content">
        <div class="project-detail-container">
            <!-- Breadcrumb -->
            <div class="project-breadcrumb">
                <a href="${pageContext.request.contextPath}/app/projects">Projects</a> / ${requestScope.project.name}
            </div>

            <!-- Project Header -->
            <div class="project-detail-header">
                <div class="project-title-row">
                    <div class="project-detail-icon">
                        <i class="fas fa-hashtag"></i>
                    </div>
                    <h1>${requestScope.project.name}</h1>
                </div>
            </div>

            <!-- Tasks Section -->
            <div class="project-tasks-section">
                <div class="section-header">
                    <h2><i class="fas fa-tasks"></i> Tasks</h2>
                </div>

                <c:choose>
                    <c:when test="${empty requestScope.projectTasks}">
                        <div class="project-tasks-empty">
                            <i class="fas fa-inbox"></i>
                            <p>No tasks in this project yet.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="project-tasks-table">
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Due Date</th>
                                    <th>Priority</th>
                                    <th>Status</th>
                                    <th>Created</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="t" items="${requestScope.projectTasks}">
                                    <tr>
                                        <td>${t.title}</td>
                                        <td><c:out value='${t.dueDate}'/></td>
                                        <td>
                                            <span class="priority-${t.priority}">
                                                <i class="fas fa-flag"></i> P${t.priority}
                                            </span>
                                        </td>
                                        <td>
                                            <span class="status-badge ${t.completedAt != null ? 'completed' : 'pending'}">
                                                ${t.completedAt != null ? 'Completed' : 'Pending'}
                                            </span>
                                        </td>
                                        <td>${t.createdAt}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
</div>
</body>
</html>
