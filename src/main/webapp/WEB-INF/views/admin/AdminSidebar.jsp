<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<aside class="admin-sidebar">
    <div class="admin-sidebar-header">
        <h3><i class="fas fa-shield-alt"></i> Admin Panel</h3>
        <div class="admin-user-info">
            Welcome, <strong><c:out value="${sessionScope.currentUser.username}"/></strong>
        </div>
    </div>
    
    <nav class="admin-nav">
        <a href="${pageContext.request.contextPath}/admin" class="admin-nav-item">
            <i class="fas fa-tachometer-alt"></i> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/admin/users" class="admin-nav-item">
            <i class="fas fa-users"></i> Users
        </a>
        <a href="${pageContext.request.contextPath}/admin/tasks" class="admin-nav-item">
            <i class="fas fa-tasks"></i> Tasks
        </a>
        <a href="${pageContext.request.contextPath}/admin/projects" class="admin-nav-item">
            <i class="fas fa-folder"></i> Projects
        </a>
        
        <div class="admin-nav-divider"></div>
        
        <a href="${pageContext.request.contextPath}/app/inbox" class="admin-nav-item">
            <i class="fas fa-home"></i> View App
        </a>
        <a href="${pageContext.request.contextPath}/auth/logout" class="admin-nav-item">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>
    </nav>
</aside>