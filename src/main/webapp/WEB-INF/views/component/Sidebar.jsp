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

    <!-- Global Search Bar -->
    <div class="sidebar-search">
        <jsp:include page="/WEB-INF/views/component/SearchBar.jsp"/>
    </div>

    <div class="sidebar-actions">
        <a href="javascript:void(0)" onclick="openAddTaskModal()" class="btn-add-task-quick">
            <i class="fa-solid fa-circle-plus"></i> Add task
        </a>
    </div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/app/inbox"
           class="sidebar-item ${param.active == 'inbox' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-solid fa-inbox icon-inbox"></i> Inbox
            </div>
            <span class="item-count">4</span>
        </a>
        <a href="${pageContext.request.contextPath}/app/today"
           class="sidebar-item ${param.active == 'today' ? 'active' : ''}">
            <div class="sidebar-item-content">
                <i class="fa-regular fa-calendar icon-today"></i> Today
            </div>
            <span class="item-count">4</span>
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
                <span>My Projects</span>
                <i class="fa-solid fa-plus add-project-icon"></i>
            </div>
            <a href="${pageContext.request.contextPath}/app/projects" class="sidebar-item">
                <div class="sidebar-item-content">
                    <i class="fa-solid fa-hashtag project-dot"></i> Projects
                </div>
            </a>
        </div>
    </nav>

    <div class="sidebar-footer">
        <a href="#" class="footer-item">
            <i class="fa-regular fa-circle-question"></i> Help & Feedback
        </a>
    </div>
</aside>