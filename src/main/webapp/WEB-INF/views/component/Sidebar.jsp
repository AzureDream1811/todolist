<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/4/2025
  Time: 12:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/searchBar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/addProject.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">

<aside class="sidebar">
    <div class="user-profile-container">
        <div class="user-profile">
            <div class="user-info">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser.avatar}">
                        <img src="${pageContext.request.contextPath}/${sessionScope.currentUser.avatar}" 
                             alt="Avatar" 
                             class="user-avatar-img"
                             style="width: 26px; height: 26px; max-width: 26px; max-height: 26px; border-radius: 50%; object-fit: cover;">
                    </c:when>
                    <c:otherwise>
                        <div class="user-avatar">
                            ${sessionScope.currentUser.username.substring(0,1).toUpperCase()}
                        </div>
                    </c:otherwise>
                </c:choose>
                <span class="user-name">${sessionScope.currentUser.username}</span>
                <i class="fa-solid fa-chevron-down toggle-icon"></i>
            </div>
            <div class="profile-actions">
                <i class="fa-regular fa-bell"></i>
                <i class="fa-solid fa-sidebar"></i>
            </div>
        </div>

        <div class="user-dropdown-card">
            <div class="dropdown-header">
                <span class="full-name">${sessionScope.currentUser.username}</span>
                <span class="task-summary">0/5 tasks</span>
            </div>

            <div class="dropdown-divider"></div>
            
            <%-- Admin Panel Link (only visible to admins) --%>
            <c:if test="${sessionScope.currentUser.role eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="dropdown-item admin-link">
                    <i class="fa-solid fa-shield-halved"></i>
                    <span>Admin Panel</span>
                </a>
            </c:if>
            
            <a href="${pageContext.request.contextPath}/app/profile" class="dropdown-item">
                <i class="fa-solid fa-user-circle"></i>
                <span>My Profile</span>
            </a>
            
            <a href="${pageContext.request.contextPath}/auth/logout" class="dropdown-item logout-link">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span>Log out</span>
            </a>
        </div>
    </div>

    <div class="sidebar-actions">
        <a href="javascript:void(0)" onclick="openAddTaskModal()" class="btn-add-task-quick">
            <i class="fa-solid fa-circle-plus"></i> Add task
        </a>
    </div>

    <nav class="sidebar-nav">
        <!-- Global Search Bar -->
        <a href="javascript:void(0)" onclick="openSearchModal()"
           class="sidebar-item ${param.active == 'search' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-solid fa-magnifying-glass icon-search"></i> Search
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/app/inbox"
           class="sidebar-item ${param.active == 'inbox' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-solid fa-inbox icon-inbox"></i> Inbox
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/app/today"
           class="sidebar-item ${param.active == 'today' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-regular fa-calendar icon-today"></i> Today
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/app/upcoming"
           class="sidebar-item ${param.active == 'upcoming' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-regular fa-calendar-days icon-upcoming"></i> Upcoming
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/app/completed"
           class="sidebar-item ${param.active == 'completed' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-solid fa-circle-check icon-completed"></i> Completed
            </div>
        </a>

        <div class="sidebar-section">
            <div class="sidebar-section-title">
                <a href="${pageContext.request.contextPath}/app/projects"
                   class="project-title-link"
                   style="text-decoration: none; color: inherit;">
                    <span>My Projects</span>
                </a>
                <i class="fa-solid fa-plus add-project-icon" onclick="openAddProjectModal()"></i>
            </div>
        </div>
    </nav>

    <div class="sidebar-footer">
        <a href="#" class="footer-item">
            <i class="fa-regular fa-circle-question"></i> Help & Feedback
        </a>
    </div>
</aside>

<script>
    function toggleAddProject() {
        const container = document.getElementById('add-project-container');
        // Kiểm tra nếu container tồn tại để tránh lỗi console
        if (container) {
            if (container.style.display === 'none' || container.style.display === '') {
                container.style.display = 'block';
            } else {
                container.style.display = 'none';
            }
        }
    }

    // Mở Modal Add Project
    function openAddProjectModal() {
        const modal = document.getElementById('addProjectModal');
        if (modal) {
            modal.style.display = 'flex';
            // Focus vào ô input ngay khi mở
            setTimeout(() => {
                document.getElementById('projectName').focus();
            }, 100);
        }
    }

    // Đóng Modal Add Project
    function closeAddProjectModal(event) {
        const modal = document.getElementById('addProjectModal');
        if (modal) {
            modal.style.display = 'none';
        }
    }
    // Đóng khi nhấn Esc
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeAddProjectModal();
        }
    });
</script>