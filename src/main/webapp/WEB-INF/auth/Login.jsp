<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 10/31/2025
  Time: 12:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}/login">
    <table>
        <tr>
            <td><label for="username"></label>Username</td>
            <td><input type="text" name="username" id="username" size="37"></td>
        </tr>
        <tr>
            <td><label for="password"></label>Password</td>
            <td><input type="password" name="password" id="password" size="37"></td>
        </tr>
        <tr>
            <td>
                <a href="${pageContext.request.contextPath}/register">
                    <button type="button">Register</button>
                </a>
            </td>
            <td>
                <button type="submit">Login</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
