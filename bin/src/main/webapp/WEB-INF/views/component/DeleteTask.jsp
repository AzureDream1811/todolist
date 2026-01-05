<%--
  Created by IntelliJ IDEA.
  User: Hi
  Date: 15/11/2025
  Time: 3:39 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Delete Task</title>
</head>
<body>
<h2>Delete Task</h2>

<%-- Hiển thị lỗi --%>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<form action="${pageContext.request.contextPath}/app/delete-task" method="post" id="deleteForm">
    <table>
        <tr>
            <td>
                <label for="taskIdInput">Task ID:</label>
            </td>
            <td>
                <input type="text" id="taskIdInput" name="taskID" placeholder="Enter Task ID" value="${param.taskID}">
            </td>
        </tr>
        <tr>
            <td>
                <button type="reset">Cancel</button>
                <button type="submit" onclick="return confirmDelete()">Delete</button>
            </td>
        </tr>
    </table>

    <input type="hidden" name="confirm" value="true">
</form>

<script>
    function confirmDelete() {
        const taskId = document.getElementById('taskIdInput').value;
        if (!taskId) {
            alert('Please enter a Task ID');
            return false;
        }
        return confirm('Are you sure you want to delete task ID: ' + taskId + '? This action cannot be undone.');
    }
</script>

</body>
</html>