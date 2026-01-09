<%-- Created by IntelliJ IDEA. User: ducph Date: 11/1/2025 Time: 5:03 PM To
change this template use File | Settings | File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Projects</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/projects.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="app-container">
    <!-- add sidebar-->
    <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
        <jsp:param name="active" value="projects"/>
    </jsp:include>

    <main class="main-content">
        <div class="projects-container">
            <div class="projects-header">
                <h1>Projects</h1>
            </div>

            <%-- error --%>
            <c:if test="${not empty requestScope.error}">
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty requestScope.projects}">
                    <div class="projects-empty">
                        <i class="fas fa-folder-open"></i>
                        <h3>No projects yet</h3>
                        <p>Create your first project to organize your tasks</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="projects-list">
                        <c:forEach var="project" items="${requestScope.projects}">
                            <div class="project-card">
                                <a href="${pageContext.request.contextPath}/app/projects?id=${project.id}" 
                                   style="text-decoration: none; color: inherit;">
                                    <div class="project-card-header">
                                        <div class="project-icon">
                                            <i class="fas fa-hashtag"></i>
                                        </div>
                                        <span class="project-name">${project.name}</span>
                                    </div>
                                    <div class="project-card-body">
                                        <div class="project-meta">
                                            <i class="far fa-calendar"></i>
                                            Created ${project.createdAt}
                                        </div>
                                    </div>
                                </a>
                                <div class="project-card-footer">
                                    <form action="${pageContext.request.contextPath}/projects/delete" method="post" 
                                          style="display: inline;" 
                                          onsubmit="return confirm('Are you sure you want to delete this project?');">
                                        <input type="hidden" name="id" value="${project.id}">
                                        <button type="submit" class="project-action-btn delete" title="Delete project">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

            <jsp:include page="/WEB-INF/views/component/AddProject.jsp">
                <jsp:param name="active" value="projects"/>
            </jsp:include>
        </div>
    </main>
</div>
</body>
</html>