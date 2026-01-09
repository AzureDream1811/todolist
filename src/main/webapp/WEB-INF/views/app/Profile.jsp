<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile - TodoList</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
</script>
</body>
</html>
