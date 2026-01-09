<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/1/2025
  Time: 5:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="addTaskModal" class="task-modal">
    <div class="task-modal-content">
        <form action="${pageContext.request.contextPath}/tasks/add" method="post" id="taskForm">
            <div class="task-input-section">
                <input type="text" name="title" class="input-title" placeholder="Task name" required>
                <textarea name="description" class="input-desc" placeholder="Description"></textarea>
            </div>

            <div class="task-options-row">
                <%-- 1. Xử lý Ngày tháng --%>
                <div class="option-item">
                    <i class="fa-regular fa-calendar"></i>
                    <c:choose>
                        <c:when test="${param.taskType == 'today'}">
                            <input type="hidden" name="dueDate" value="<%= java.time.LocalDate.now() %>">
                            <span class="date-text" style="color: #058527; font-weight: bold;">Today</span>
                        </c:when>
                        <c:otherwise>
                            <input type="date" name="dueDate" class="inline-date-picker">
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- 2. Xử lý Độ ưu tiên --%>
                <div class="option-item">
                    <select name="priority" class="inline-select">
                        <option value="1">Priority 1</option>
                        <option value="2">Priority 2</option>
                        <option value="3">Priority 3</option>
                        <option value="4" selected>Priority 4</option>
                    </select>
                </div>

                <%-- 3. Xử lý Dự án (BỔ SUNG LẠI) --%>
                <div class="option-item">
                    <i class="fa-solid fa-list-ul"></i>
                    <select name="projectId" class="inline-select">
                        <option value="0">Inbox</option>
                        <c:forEach var="project" items="${requestScope.projects}">
                            <option value="${project.id}" ${param.projectIdParam == project.id ? 'selected' : ''}>${project.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="task-modal-footer">
                <button type="button" class="btn-cancel" onclick="closeAddTaskModal()">Cancel</button>
                <button type="submit" class="btn-add">Add task</button>
            </div>

            <input type="hidden" name="taskType" value="${param.taskType}">
            <input type="hidden" name="redirectProjectId" value="${param.projectIdParam}">
            <input type="hidden" name="completed" value="false">
        </form>
    </div>
</div>
