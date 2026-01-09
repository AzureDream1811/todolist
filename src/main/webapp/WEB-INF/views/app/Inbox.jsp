<%--
  Created by IntelliJ IDEA.
  User: ducph
  Date: 11/1/2025
  Time: 11:49 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Inbox</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/inbox.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/addTask.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="app-container">
    <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
        <jsp:param name="active" value="inbox"/>
    </jsp:include>

    <main class="main-content">
        <div class="content-wrapper">
            <header class="page-header">
                <h1 class="page-title">Inbox</h1>
            </header>

            <c:if test="${not empty requestScope.error}">
                <div class="error-text">
                    <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
                </div>
            </c:if>

            <div class="task-list">
                <%-- Khai báo ngày hiện tại một lần ở ngoài vòng lặp --%>
                <c:set var="today" value="<%= LocalDate.now() %>" />

                <%-- BẮT BUỘC PHẢI CÓ VÒNG LẶP NÀY --%>
                <c:forEach var="task" items="${requestScope.tasks}">
                    <div class="task-item" id="task-row-${task.id}">
                            <%-- Nút hoàn thành task --%>
                        <button type="button" class="task-check-btn" title="Mark as complete" onclick="handleComplete(${task.id})"></button>

                        <div class="task-info">
                            <div class="task-title">${task.title}</div>
                            <c:if test="${not empty task.description}">
                                <div class="task-desc">${task.description}</div>
                            </c:if>

                                <%-- Hiển thị Lịch ấn được --%>
                            <c:if test="${not empty task.dueDate}">
                                <div class="task-meta-wrapper ${task.dueDate.isBefore(today) ? 'is-overdue' : ''}">
                                    <label class="calendar-trigger">
                                        <input type="date"
                                               value="${task.dueDate}"
                                               class="inline-date-picker"
                                               onchange="updateTaskDate(${task.id}, this.value)">
                                    </label>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="inline-add-task-container" style="margin-top: 20px;">
                <button id="showAddTaskBtn" class="add-task-btn" onclick="toggleAddTask(true)">
                    <span class="plus-icon">+</span> Add task
                </button>

                <div id="inlineAddTaskForm" style="display: none;">
                    <jsp:include page="../component/AddTask.jsp">
                        <jsp:param name="taskType" value="inbox"/>
                    </jsp:include>
                </div>
            </div>
        </div>
    </main>
</div>

<div id="sidebarAddTaskModal" class="modal-overlay">
    <div class="modal-box">
        <jsp:include page="../component/AddTask.jsp">
            <jsp:param name="taskType" value="inbox"/>
        </jsp:include>
    </div>
</div>

<script>
    function handleComplete(taskId) {
        const taskRow = document.getElementById('task-row-' + taskId);

        // 1. Chạy hiệu ứng mờ dần và trượt (đã thêm trong app.css)
        if (taskRow) {
            taskRow.classList.add('fade-out');
        }

        // 2. Gọi API hoàn thành task về TaskServlet
        // Lưu ý: URL là /tasks/complete vì Servlet của bạn mapping là @WebServlet("/tasks/*")
        fetch('${pageContext.request.contextPath}/tasks/complete?taskId=' + taskId, {
            method: 'POST'
        })
            .then(response => {
                if (response.ok) {
                    // 3. Đợi hiệu ứng CSS kết thúc (0.4s) rồi xóa hẳn phần tử khỏi giao diện
                    setTimeout(() => {
                        if (taskRow) taskRow.remove();
                    }, 400);
                } else {
                    // Nếu server lỗi, hiển thị lại task
                    if (taskRow) taskRow.classList.remove('fade-out');
                    alert("Không thể hoàn thành task. Vui lòng thử lại!");
                }
            })
            .catch(error => {
                if (taskRow) taskRow.classList.remove('fade-out');
                console.error("Lỗi kết nối:", error);
            });
    }

    // 1. Xử lý nút Sidebar (Hiện đè lên màn hình)
    function openAddTaskModal() {
        const modal = document.getElementById("sidebarAddTaskModal");
        modal.style.display = "flex"; // Hiện lớp phủ mờ
    }

    // 2. Xử lý nút trong trang (Hiện tại chỗ)
    function toggleAddTask(show) {
        const btn = document.getElementById("showAddTaskBtn");
        const form = document.getElementById("inlineAddTaskForm");

        if (show) {
            btn.style.display = "none";
            form.style.display = "block";
            const input = form.querySelector('.input-title');
            if (input) input.focus();
        } else {
            btn.style.display = "flex";
            form.style.display = "none";
        }
    }

    // 3. Hàm đóng chung cho nút Cancel
    function closeAddTaskModal() {
        // Đóng modal sidebar
        document.getElementById("sidebarAddTaskModal").style.display = "none";
        // Đóng inline
        toggleAddTask(false);
    }

    // Đóng modal khi click ra ngoài vùng trắng
    window.onclick = function (event) {
        const modal = document.getElementById("sidebarAddTaskModal");
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    // Hàm xử lý đổi ngày nhanh khi chọn từ lịch
    function updateTaskDate(taskId, newDate) {
        if (!newDate) return;
        // Gửi fetch về AppServlet hoặc TaskServlet để update dueDate
        fetch('${pageContext.request.contextPath}/tasks/update', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'id=' + taskId + '&dueDate=' + newDate
        }).then(response => {
            if (response.ok) location.reload();
        });
    }
</script>
</body>
</html>
