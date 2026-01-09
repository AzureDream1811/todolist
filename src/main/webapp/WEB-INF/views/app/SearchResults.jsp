<%--
  Search Results Page
  Created: 09/01/2026
  Description: Displays search results for tasks and projects
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Search Results - ${param.q}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/searchBar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/searchResults.css">
</head>
<body>
    <div class="app-layout">
        <!-- Sidebar -->
        <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
            <jsp:param name="active" value="search"/>
        </jsp:include>
        
        <!-- Main Content -->
        <main class="main-content">
            <div class="search-results-container">
                <!-- Search Header with Search Bar -->
                <div class="search-results-header">
                    <h2>Search Results</h2>
                    <p>
                        <c:choose>
                            <c:when test="${totalResults > 0}">
                                Found ${totalResults} result(s) for "<strong>${searchQuery}</strong>"
                            </c:when>
                            <c:otherwise>
                                No results found for "<strong>${searchQuery}</strong>"
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <c:choose>
                    <c:when test="${totalResults > 0}">
                        <!-- Tasks Section -->
                        <c:if test="${not empty tasks}">
                            <div class="search-results-section">
                                <h3>
                                    <i class="fa-solid fa-circle-check"></i>
                                    Tasks (${tasks.size()})
                                </h3>
                                <div class="search-results-list">
                                    <c:forEach var="task" items="${tasks}">
                                        <a href="${pageContext.request.contextPath}/app/inbox?taskId=${task.id}" 
                                           class="search-result-card">
                                            <i class="fa-regular fa-circle"></i>
                                            <div class="result-content">
                                                <div class="result-title">${task.title}</div>
                                                <div class="result-meta">
                                                    <c:if test="${not empty task.dueDate}">
                                                        <span><i class="fa-regular fa-calendar"></i> ${task.dueDate}</span>
                                                    </c:if>
                                                    <c:if test="${task.priority > 0}">
                                                        <span><i class="fa-solid fa-flag"></i> Priority ${task.priority}</span>
                                                    </c:if>
                                                </div>
                                            </div>
                                            <i class="fa-solid fa-chevron-right" style="color: #ccc;"></i>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                        
                        <!-- Projects Section -->
                        <c:if test="${not empty projects}">
                            <div class="search-results-section">
                                <h3>
                                    <i class="fa-solid fa-folder"></i>
                                    Projects (${projects.size()})
                                </h3>
                                <div class="search-results-list">
                                    <c:forEach var="project" items="${projects}">
                                        <a href="${pageContext.request.contextPath}/app/projects?id=${project.id}" 
                                           class="search-result-card">
                                            <i class="fa-solid fa-hashtag"></i>
                                            <div class="result-content">
                                                <div class="result-title">${project.name}</div>
                                                <div class="result-meta">
                                                    <span><i class="fa-regular fa-calendar"></i> Created ${project.createdAt}</span>
                                                </div>
                                            </div>
                                            <i class="fa-solid fa-chevron-right" style="color: #ccc;"></i>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <!-- Empty State -->
                        <div class="search-empty-state">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <h3>No results found</h3>
                            <p>Try searching with different keywords</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</body>
</html>
