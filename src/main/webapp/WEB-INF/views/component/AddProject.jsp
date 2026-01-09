<%--
  Created by GitHub Copilot assistant.
  Purpose: Add New Project dialog (modeled on AddTask.jsp)
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>Add New Project</h2>

<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/projects/add">
    <div class="form-group">
        <label for="projectName">Project Name:</label>
        <input type="text" id="projectName" name="name" 
               required class="form-control" 
               placeholder="Enter project name" autofocus>
    </div>

    <c:if test="${not empty requestScope.error}">
        <div class="alert alert-danger" role="alert">
            ${requestScope.error}
        </div>
    </c:if>

    <div class="form-group mt-3">
        <a href="${pageContext.request.contextPath}/projects" 
           class="btn btn-secondary">Cancel</a>
        <button type="submit" class="btn btn-primary">Add Project</button>
    </div>
</form>
</body>


</html>