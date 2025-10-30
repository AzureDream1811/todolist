<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 10/31/2025
  Time: 12:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page isELIgnored="false" %>
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
                <button name="register">Register</button>
            </td>
            <td>
                <input type="submit" value="Login">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
