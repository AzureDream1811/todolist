<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Projects - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp"/>

    <div class="admin-content">
        <div class="admin-page-header">
            <h1><i class="fas fa-folder"></i> Projects Management</h1>
            <p>View and manage all projects across users</p>
        </div>

        <c:if test="${not empty requestScope.error}">
            <div class="admin-alert error">
                <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
            </div>
        </c:if>

        <div class="admin-table-container">
            <div class="admin-table-header">
                <h2>All Projects</h2>
            </div>
            
            <c:choose>
                <c:when test="${empty requestScope.projects}">
                    <div class="admin-empty">
                        <i class="fas fa-folder-open"></i>
                        <h3>No projects found</h3>
                        <p>There are no projects in the system yet.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>User ID</th>
                                <th>Created</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="project" items="${requestScope.projects}">
                                <tr>
                                    <td>${project.id}</td>
                                    <td>${project.name}</td>
                                    <td>${project.userId}</td>
                                    <td>${project.createdAt}</td>
                                    <td>
                                        <div class="table-actions">
                                            <form action="${pageContext.request.contextPath}/admin/projects/delete" method="post" style="display:inline;"
                                                  onsubmit="return confirm('Are you sure you want to delete this project?');">
                                                <input type="hidden" name="projectId" value="${project.id}">
                                                <button type="submit" class="table-btn delete" title="Delete Project">
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