<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile - TodoList</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/component/addTask.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/inbox.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/profile.css">
</head>
<body>
<div class="app-container">
    <jsp:include page="/WEB-INF/views/component/Sidebar.jsp">
        <jsp:param name="active" value="profile"/>
    </jsp:include>

    <main class="main-content">
        <div class="profile-container">
            <div class="profile-header">
                <h1><i class="fas fa-user-circle"></i> My Profile</h1>
                <p>Manage your account settings and profile picture</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>
            
            <c:if test="${param.success == 'avatar_updated'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> Profile picture updated successfully!
                </div>
            </c:if>
            
            <c:if test="${param.success == 'avatar_removed'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> Profile picture removed successfully!
                </div>
            </c:if>

            <div class="profile-content">
                <!-- Avatar Section -->
                <div class="profile-card avatar-section">
                    <h2><i class="fas fa-camera"></i> Profile Picture</h2>
                    
                    <div class="avatar-preview-container">
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser.avatar}">
                                <img src="${pageContext.request.contextPath}/${sessionScope.currentUser.avatar}" 
                                     alt="Profile Avatar" 
                                     class="avatar-preview"
                                     id="avatarPreview">
                            </c:when>
                            <c:otherwise>
                                <div class="avatar-placeholder" id="avatarPlaceholder">
                                    <span>${sessionScope.currentUser.username.substring(0,1).toUpperCase()}</span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <form action="${pageContext.request.contextPath}/app/profile" 
                          method="post" 
                          enctype="multipart/form-data"
                          class="avatar-form">
                        <input type="hidden" name="action" value="uploadAvatar">
                        
                        <div class="file-input-wrapper">
                            <label for="avatarInput" class="file-input-label">
                                <i class="fas fa-upload"></i> Choose Image
                            </label>
                            <input type="file" 
                                   id="avatarInput" 
                                   name="avatar" 
                                   accept="image/jpeg,image/png,image/gif,image/webp"
                                   onchange="previewImage(this)">
                            <span class="file-name" id="fileName">No file chosen</span>
                        </div>

                        <p class="file-hint">
                            <i class="fas fa-info-circle"></i> 
                            Accepted formats: JPG, PNG, GIF, WebP. Max size: 5MB
                        </p>

                        <div class="avatar-actions">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Upload Picture
                            </button>
                            
                            <c:if test="${not empty sessionScope.currentUser.avatar}">
                                <button type="button" class="btn btn-danger" onclick="removeAvatar()">
                                    <i class="fas fa-trash"></i> Remove Picture
                                </button>
                            </c:if>
                        </div>
                    </form>

                    <!-- Hidden form for removing avatar -->
                    <form id="removeAvatarForm" 
                          action="${pageContext.request.contextPath}/app/profile" 
                          method="post" 
                          style="display: none;">
                        <input type="hidden" name="action" value="removeAvatar">
                    </form>
                </div>

                <!-- User Info Section -->
                <div class="profile-card info-section">
                    <h2><i class="fas fa-id-card"></i> Account Information</h2>
                    
                    <div class="info-grid">
                        <div class="info-item">
                            <label><i class="fas fa-user"></i> Username</label>
                            <span>${sessionScope.currentUser.username}</span>
                        </div>
                        
                        <div class="info-item">
                            <label><i class="fas fa-envelope"></i> Email</label>
                            <span>${sessionScope.currentUser.email}</span>
                        </div>
                        
                        <div class="info-item">
                            <label><i class="fas fa-calendar"></i> Member Since</label>
                            <span>${sessionScope.currentUser.createdAt}</span>
                        </div>
                        
                        <div class="info-item">
                            <label><i class="fas fa-shield-alt"></i> Role</label>
                            <span class="role-badge role-${sessionScope.currentUser.role.toLowerCase()}">
                                ${sessionScope.currentUser.role}
                            </span>
                        </div>
                    </div>
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

<script>
    function previewImage(input) {
        const fileName = document.getElementById('fileName');
        const preview = document.getElementById('avatarPreview');
        const placeholder = document.getElementById('avatarPlaceholder');
        
        if (input.files && input.files[0]) {
            const file = input.files[0];
            fileName.textContent = file.name;
            
            // Validate file size
            if (file.size > 5 * 1024 * 1024) {
                alert('File size must be less than 5MB');
                input.value = '';
                fileName.textContent = 'No file chosen';
                return;
            }
            
            const reader = new FileReader();
            reader.onload = function(e) {
                if (preview) {
                    preview.src = e.target.result;
                } else if (placeholder) {
                    // Replace placeholder with image
                    const container = placeholder.parentElement;
                    placeholder.remove();
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    img.className = 'avatar-preview';
                    img.id = 'avatarPreview';
                    img.alt = 'Profile Avatar';
                    img.style.width = '150px';
                    img.style.height = '150px';
                    img.style.maxWidth = '150px';
                    img.style.maxHeight = '150px';
                    img.style.objectFit = 'cover';
                    img.style.borderRadius = '50%';
                    container.appendChild(img);
                }
            };
            reader.readAsDataURL(file);
        }
    }
    
    function removeAvatar() {
        if (confirm('Are you sure you want to remove your profile picture?')) {
            document.getElementById('removeAvatarForm').submit();
        }
    }

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
