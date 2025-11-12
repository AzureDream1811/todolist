<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/1/2025
  Time: 5:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Add New Task</h2>

<%-- error --%>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>
<form action="${pageContext.request.contextPath}/app/tasks" method="post" id="taskForm" onsubmit="return validateForm()">
    <table>
        <tr>
            <td>
                <label>
                    <input type="text" name="title" placeholder="Add new task">
                </label>
            </td>
            <td>
                <label>
                    <input type="text" name="description" placeholder="Description">
                </label>
            </td>
            <%--Display DueDate depending on taskType--%>
            <c:choose>
                <c:when test="${param.taskType == 'today'}">
                    <input type="hidden" name="dueDate" value="<%= java.time.LocalDate.now() %>">
                    <td>today</td>
                </c:when>
                <c:when test="${param.taskType == 'upcoming'}">
                    <td>
                        <input type="date" name="dueDate" pattern="\d{4}-\d{2}-\d{2}">
                    </td>
                </c:when>
                <c:otherwise>
                    <td>
                        <input type="date" name="dueDate" pattern="\d{4}-\d{2}-\d{2}">
                    </td>
                </c:otherwise>
            </c:choose>
            <td>
                <label>
                    <select name="priority" id="priority">
                        <option value="">priority</option>
                        <option value="1" selected>1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                    </select>
                </label>
            </td>
            <td>
                <label>
                    <select name="projectId">
                        <option value="">Project</option>
                        <c:forEach var="project" items="${requestScope.projects}">
                            <option value="${project.id}">${project.name}</option>
                        </c:forEach>
                    </select>
                </label>
            </td>
            <td>
                <button type="reset">Cancel</button>
                <button type="submit">Add Task</button>
            </td>
        </tr>
    </table>

    <%-- Hidden fields required for the form --%>
    <input type="hidden" name="taskType" value="${param.taskType}">
    <input type="hidden" name="completed" value="false">
    <input type="hidden" name="projectId" value="0">

</form>
</body>
</html>
