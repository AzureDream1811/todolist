<%--
  Created by IntelliJ IDEA.
  User: Hi
  Date: 01/11/2025
  Time: 1:06 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<html>
<head>
    <title>Register</title>
</head>
<body>

<jsp:useBean id="error" scope="request" type="java.lang.String"/>
<c:if test="${not empty error}">
    <div style="color: red;">${error}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/register">
    <table>
        <tr>
            <td><label for="username"></label>Username</td>
            <td><input type="text" name="username" id="username" value="${param.username}" size="37" required></td>
        </tr>
        <tr>
            <td><label for="password"></label>Password</td>
            <td><input type="password" name="password" id="password" size="37" required></td>
        </tr>
        <tr>
            <td><label for="confirmPassword"></label>confirmPassword</td>
            <td><input type="password" name="confirmPassword" id="confirmPassword" size="37" required></td>
        </tr>
        <tr>
            <td><label for="email"></label>email</td>
            <td><input type="email" name="email" id="email" value="${param.email}" size="37"></td>
        </tr>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/login">
                    <button type="button">Login</button>
                </a>
            </td>
            <td>
                <input type="submit" value="Register">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
