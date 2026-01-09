<%-- Created by IntelliJ IDEA. User: ducph Date: 11/1/2025 Time: 5:03 PM To
change this template use File | Settings | File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Projects</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/addTask.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/inbox.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/projects.css">
</head>
<body>
<div class="app-container">
    <!-- add sidebar-->
    <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
        <jsp:param name="active" value="projects"/>
    </jsp:include>

    <main class="main-content">
        <div class="projects-container">
            <div class="projects-header">
                <h1>Projects</h1>
            </div>

            <%-- error --%>
            <c:if test="${not empty requestScope.error}">
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty requestScope.projects}">
                    <div class="projects-empty">
                        <i class="fas fa-folder-open"></i>
                        <h3>No projects yet</h3>
                        <p>Create your first project to organize your tasks</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="projects-list">
                        <c:forEach var="project" items="${requestScope.projects}">
                            <div class="project-card">
                                <a href="${pageContext.request.contextPath}/app/projects?id=${project.id}" 
                                   style="text-decoration: none; color: inherit;">
                                    <div class="project-card-header">
                                        <div class="project-icon">
                                            <i class="fas fa-hashtag"></i>
                                        </div>
                                        <span class="project-name">${project.name}</span>
                                    </div>
                                    <div class="project-card-body">
                                        <div class="project-meta">
                                            <i class="far fa-calendar"></i>
                                            Created ${project.createdAt}
                                        </div>
                                    </div>
                                </a>
                                <div class="project-card-footer">
                                    <form action="${pageContext.request.contextPath}/projects/delete" method="post" 
                                          style="display: inline;" 
                                          onsubmit="return confirm('Are you sure you want to delete this project?');">
                                        <input type="hidden" name="id" value="${project.id}">
                                        <button type="submit" class="project-action-btn delete" title="Delete project">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
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

<div id="addProjectModal" class="modal-overlay">
    <div class="modal-box">
        <jsp:include page="../component/AddProject.jsp">
            <jsp:param name="taskType" value="project"/>
        </jsp:include>
    </div>
</div>

<div id="searchModal" class="modal-overlay">
    <div class="modal-box" style="max-width: 600px;">
        <jsp:include page="/WEB-INF/views/component/SearchBar.jsp" />
    </div>
</div>

<%-- Edit Task Modal --%>
<jsp:include page="../component/EditTask.jsp"/>

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
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'id=' + taskId + '&dueDate=' + newDate
        }).then(response => {
            if (response.ok) location.reload();
        });
    }

    function handleDelete(taskId) {
        // Xác nhận trước khi xóa
        if (!confirm('Are you sure you want to delete this task?')) {
            return;
        }

        const taskRow = document.getElementById('task-row-' + taskId);

        // 1. Chạy hiệu ứng mờ dần
        if (taskRow) {
            taskRow.classList.add('fade-out');
        }

        // 2. Gọi API xóa task
        fetch('${pageContext.request.contextPath}/tasks/delete', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'taskId=' + taskId
        })
            .then(response => {
                if (response.ok) {
                    // 3. Đợi hiệu ứng CSS kết thúc rồi xóa phần tử
                    setTimeout(() => {
                        if (taskRow) taskRow.remove();
                    }, 400);
                } else {
                    // Nếu lỗi, hiển thị lại task
                    if (taskRow) taskRow.classList.remove('fade-out');
                    alert("Không thể xóa task. Vui lòng thử lại!");
                }
            })
            .catch(error => {
                if (taskRow) taskRow.classList.remove('fade-out');
                console.error("Lỗi kết nối:", error);
                alert("Lỗi kết nối. Vui lòng thử lại!");
            });
    }

    // Handle edit task
    function handleEdit(taskId) {
        const taskRow = document.getElementById('task-row-' + taskId);
        if (taskRow) {
            const title = taskRow.dataset.taskTitle;
            const description = taskRow.dataset.taskDescription;
            const dueDate = taskRow.dataset.taskDueDate;
            const priority = taskRow.dataset.taskPriority;
            const projectId = taskRow.dataset.taskProjectId;
            openEditTaskModal(taskId, title, description, dueDate, priority, projectId);
        }
    }

    // Mở Modal Add Project
    function openAddProjectModal() {
        const modal = document.getElementById('addProjectModal');
        if (modal) {
            modal.style.display = 'flex';
            // Focus vào ô input ngay khi mở
            setTimeout(() => {
                document.getElementById('projectName').focus();
            }, 100);
        }
    }

    // Đóng Modal Add Project
    function closeAddProjectModal(event) {
        const modal = document.getElementById('addProjectModal');
        if (modal) {
            modal.style.display = 'none';
        }
    }
    // Đóng khi nhấn Esc
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeAddProjectModal();
        }
    });

    // 1. Hàm mở Search (Gọi từ Sidebar)
    function openSearchModal() {
        const modal = document.getElementById('searchModal');
        if (modal) {
            modal.style.display = 'flex';
            // Focus vào ô input tìm kiếm ngay lập tức
            setTimeout(() => {
                const input = document.getElementById('globalSearchInput');
                if (input) input.focus();
            }, 100);
        }
    }

    // 2. Hàm đóng Search
    function closeSearchModal() {
        document.getElementById('searchModal').style.display = 'none';
    }

    // 3. Cập nhật window.onclick để xử lý tất cả các Modal một cách độc lập
    window.onclick = function (event) {
        const taskModal = document.getElementById("sidebarAddTaskModal");
        const projectModal = document.getElementById("addProjectModal");
        const searchModal = document.getElementById("searchModal");

        if (event.target == taskModal) {
            taskModal.style.display = "none";
        }
        if (event.target == projectModal) {
            projectModal.style.display = "none";
        }
        if (event.target == searchModal) {
            searchModal.style.display = "none";
        }
    }

    // 4. Hỗ trợ phím Esc để đóng Search cho nhanh
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeSearchModal();
        }
    });
</script>
</body>
</html>