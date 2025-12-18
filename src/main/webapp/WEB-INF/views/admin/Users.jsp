<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 12/17/2025
  Time: 3:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>
<jsp:include page="/WEB-INF/views/admin/AdminSidebar.jsp"/>

<div style="margin-left:220px;">
    <h2>All Users</h2>
    <c:choose>
        <c:when test="${empty requestScope.users}">
            <h2>No users</h2>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>id</th>
                    <th>username</th>
                    <th>email</th>
                    <th>createdAt</th>
                    <th>role</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${requestScope.users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.createdAt}</td>
                        <td>${user.role}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/users/delete" method="post">
                                <input type="hidden" name="userId" value="${user.id}">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${user.role eq 'ADMIN'}">
                                    <form action="${pageContext.request.contextPath}/admin/users/demote" method="post">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        <button type="submit">Demote</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/users/promote" method="post">
                                        <input type="hidden" name="userId" value="${user.id}">
                                        <button type="submit">Promote</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>

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