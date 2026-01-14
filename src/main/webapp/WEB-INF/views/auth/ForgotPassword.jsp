<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quên mật khẩu</title>
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
                    <i class="fas fa-key logo-icon" id="todo-logo"></i>
                </div>
                <h2 class="welcome-title">Quên mật khẩu?</h2>
                <p style="color: #666; margin-top: 10px; font-size: 14px;">
                    Nhập email của bạn, chúng tôi sẽ gửi link đặt lại mật khẩu
                </p>
            </div>

            <!-- Success Message -->
            <c:if test="${not empty successMessage}">
                <div class="success-message" style="background: #d4edda; color: #155724; padding: 12px; border-radius: 8px; margin-bottom: 15px; width: 100%; text-align: center;">
                    <i class="fas fa-check-circle"></i> ${successMessage}
                </div>
            </c:if>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="error-message" style="background: #f8d7da; color: #721c24; padding: 12px; border-radius: 8px; margin-bottom: 15px; width: 100%; text-align: center;">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>

            <!-- Forgot Password Form -->
            <form method="post" action="${pageContext.request.contextPath}/auth/forgot-password" class="login-form">
                <!-- Email Field -->
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="email"
                               name="email"
                               id="email"
                               class="form-input"
                               placeholder="Nhập email của bạn"
                               value="${email}"
                               required>
                    </div>
                </div>

                <!-- Divider -->
                <div class="divider"></div>

                <!-- Submit Button -->
                <div class="button-section">
                    <button type="submit" class="btn btn-login-main">
                        <i class="fas fa-paper-plane"></i> Gửi link đặt lại mật khẩu
                    </button>
                </div>

                <!-- Back to Login -->
                <div class="register-prompt" style="margin-top: 20px; text-align: center;">
                    <a href="${pageContext.request.contextPath}/auth/login" class="register-link-prompt">
                        <i class="fas fa-arrow-left"></i> Quay lại đăng nhập
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
