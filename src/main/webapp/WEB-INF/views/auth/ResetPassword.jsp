<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/auth.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-container">
        <div class="auth-form-container">
            <!-- Logo Section -->
            <div class="logo-section">
                <div class="logo-circle">
                    <i class="fas fa-lock-open logo-icon" id="todo-logo"></i>
                </div>
                <h2 class="welcome-title">Đặt lại mật khẩu</h2>
                <p style="color: #666; margin-top: 10px; font-size: 14px;">
                    Nhập mật khẩu mới cho tài khoản của bạn
                </p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="error-message" style="background: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 15px; width: 100%; text-align: center;">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <!-- Invalid/Expired Token -->
            <c:if test="${invalidToken}">
                <div class="error-message" style="background: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 15px; width: 100%; text-align: center;">
                    <i class="fas fa-exclamation-triangle"></i> Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.
                </div>
                <div class="register-prompt" style="margin-top: 20px; text-align: center;">
                    <a href="${pageContext.request.contextPath}/auth/forgot-password" class="register-link-prompt">
                        <i class="fas fa-redo"></i> Yêu cầu link mới
                    </a>
                </div>
            </c:if>

            <!-- Reset Password Form (only show if token is valid) -->
            <c:if test="${not invalidToken}">
                <form method="post" action="${pageContext.request.contextPath}/auth/reset-password" class="login-form">
                    <input type="hidden" name="token" value="${token}">
                    
                    <!-- New Password Field -->
                    <div class="form-group">
                        <div class="input-wrapper">
                            <input type="password"
                                   name="password"
                                   id="password"
                                   class="form-input"
                                   placeholder="Mật khẩu mới (ít nhất 8 ký tự)"
                                   minlength="8"
                                   required>
                        </div>
                    </div>

                    <!-- Confirm Password Field -->
                    <div class="form-group">
                        <div class="input-wrapper">
                            <input type="password"
                                   name="confirmPassword"
                                   id="confirmPassword"
                                   class="form-input"
                                   placeholder="Xác nhận mật khẩu mới"
                                   minlength="8"
                                   required>
                        </div>
                    </div>

                    <!-- Divider -->
                    <div class="divider"></div>

                    <!-- Submit Button -->
                    <div class="button-section">
                        <button type="submit" class="btn btn-login-main">
                            <i class="fas fa-save"></i> Đặt lại mật khẩu
                        </button>
                    </div>

                    <!-- Back to Login -->
                    <div class="register-prompt" style="margin-top: 20px; text-align: center;">
                        <a href="${pageContext.request.contextPath}/auth/login" class="register-link-prompt">
                            <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
                        </a>
                    </div>
                </form>
            </c:if>
        </div>
    </div>
</div>

<script>
    // Simple password match validation
    document.querySelector('form')?.addEventListener('submit', function(e) {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        if (password !== confirmPassword) {
            e.preventDefault();
            alert('Mật khẩu xác nhận không khớp!');
        }
    });
</script>
</body>
</html>
