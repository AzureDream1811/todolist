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
<!-- add sidebar-->
<jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
    <jsp:param name="active" value="projects"/>
</jsp:include>
<h2>Projects</h2>

<%-- error --%>
<c:if test="${not empty requestScope.error}">
    <p style="color: red">${requestScope.error}</p>
</c:if>
    <table>
        <tr><th>Name</th><th>Created</th></tr>
        <c:forEach var="project" items="${requestScope.projects}">
            <tr>
               <td>
               <a href="${pageContext.request.contextPath}/app/projects?id=${project.id}">
               ${project.name}
               </a>
               </td>
               <td>${project.createdAt}</td>
            </tr>
          </c:forEach>
    </table>

<script>
</script>
</body>
</html>
