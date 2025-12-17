<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<div style="border-right:1px solid #ccc;padding-right:12px;margin-right:12px;float:left;width:200px;">
    <h3>Admin</h3>
    <p style="font-size:0.9em;margin:0 0 8px 0;">Welcome, <c:out value="${sessionScope.currentUser.username}"/></p>
    <ul style="list-style:none;padding-left:0;margin:0;">
        <li style="margin-bottom:6px;"><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
        <li style="margin-bottom:6px;"><a href="${pageContext.request.contextPath}/admin/tasks">Tasks</a></li>
        <li style="margin-bottom:6px;"><a href="${pageContext.request.contextPath}/admin/projects">Projects</a></li>
        <li style="margin-bottom:6px;"><a href="${pageContext.request.contextPath}/admin">Dashboard</a></li>
        <li style="margin-top:8px;border-top:1px solid #eee;padding-top:8px;"><a href="${pageContext.request.contextPath}/app/inbox">View App</a></li>
        <li style="margin-top:6px;"><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
    </ul>
</div>