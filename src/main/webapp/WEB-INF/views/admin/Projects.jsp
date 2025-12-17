<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 12/17/2025
  Time: 4:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Projects</title>
</head>
<body>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>
<jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp"/>

<div style="margin-left:220px;">
    <h2>All Users</h2>
    <c:choose>
        <c:when test="${empty requestScope.projects}">
            <h2>No projects</h2>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>user ID</th>
                    <th>createdAt</th>
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
                            <form action="${pageContext.request.contextPath}/admin/projects/delete" method="post">
                                <input type="hidden" name="projectId" value="${project.id}">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>