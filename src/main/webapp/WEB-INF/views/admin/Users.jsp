<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp"/>

    <div class="admin-content">
        <div class="admin-page-header">
            <h1><i class="fas fa-users"></i> Users Management</h1>
            <p>Manage all registered users</p>
        </div>

        <c:if test="${not empty requestScope.error}">
            <div class="admin-alert error">
                <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
            </div>
        </c:if>

        <div class="admin-table-container">
            <div class="admin-table-header">
                <h2>All Users</h2>
            </div>
            
            <c:choose>
                <c:when test="${empty requestScope.users}">
                    <div class="admin-empty">
                        <i class="fas fa-users"></i>
                        <h3>No users found</h3>
                        <p>There are no registered users yet.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Created</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${requestScope.users}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>${user.createdAt}</td>
                                    <td>
                                        <span class="role-badge ${user.role eq 'ADMIN' ? 'admin' : 'user'}">
                                            ${user.role}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="table-actions">
                                            <c:choose>
                                                <c:when test="${user.role eq 'ADMIN'}">
                                                    <form action="${pageContext.request.contextPath}/admin/users/demote" method="post" style="display:inline;">
                                                        <input type="hidden" name="userId" value="${user.id}">
                                                        <button type="submit" class="table-btn edit" title="Demote to User">
                                                            <i class="fas fa-arrow-down"></i> Demote
                                                        </button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <form action="${pageContext.request.contextPath}/admin/users/promote" method="post" style="display:inline;">
                                                        <input type="hidden" name="userId" value="${user.id}">
                                                        <button type="submit" class="table-btn edit" title="Promote to Admin">
                                                            <i class="fas fa-arrow-up"></i> Promote
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                            <form action="${pageContext.request.contextPath}/admin/users/delete" method="post" style="display:inline;" 
                                                  onsubmit="return confirm('Are you sure you want to delete this user?');">
                                                <input type="hidden" name="userId" value="${user.id}">
                                                <button type="submit" class="table-btn delete" title="Delete User">
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