<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/auth.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-container">
        <!-- Main Form Container -->
        <div class="auth-form-container">
            <!-- Logo Section -->
            <div class="logo-section">
                <div class="logo-circle">
                    <i class="fas fa-check-circle logo-icon" id="todo-logo"></i>
                </div>
                <h2 class="welcome-title">Chào mừng bạn đến với TodoList</h2>
            </div>
            <!-- Login Form -->
            <form method="post" action="${pageContext.request.contextPath}/auth/login" class="login-form">
                <!-- Success Message (e.g., after password reset) -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="success-message" style="background: #d4edda; color: #155724; padding: 12px; border-radius: 8px; margin-bottom: 15px; width: 100%; text-align: center;">
                        <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                    </div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>

                <!-- Username Field -->
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text"
                               name="username"
                               id="username"
                               class="form-input"
                               placeholder="Username"
                               required>
                    </div>
                </div>

                <!-- Password Field -->
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password"
                               name="password"
                               id="password"
                               class="form-input"
                               placeholder="Mật khẩu"
                               required>
                    </div>
                </div>

                <!-- Register & Forgot Password -->
                <div class="register-prompt" style="display: flex; justify-content: space-between;">
                    <a href="${pageContext.request.contextPath}/auth/register" class="register-link-prompt">
                        Đăng ký tài khoản?
                    </a>
                    <a href="${pageContext.request.contextPath}/auth/forgot-password" class="register-link-prompt">
                        Quên mật khẩu?
                    </a>
                </div>

                <!-- Divider -->
                <div class="divider"></div>

                <!-- Login Button -->
                <div class="button-section">
                    <button type="submit" class="btn btn-login-main">
                        Đăng nhập
                    </button>
                </div>

                <!-- Divider with "or" -->
                <div class="divider-with-text">
                    <span class="divider-line"></span>
                    <span class="divider-or">or</span>
                    <span class="divider-line"></span>
                </div>

                <!-- Google Login Button -->
                <div class="social-login-section">
                    <button type="button" class="btn btn-google">
                        <i class="fab fa-google google-icon"></i>
                        <span class="btn-text">Tiếp tục sử dụng dịch vụ bằng Google</span>
                    </button>
                </div>

                <!-- Terms Section -->
                <div class="terms-section">
                    <p class="terms-text">
                        Bằng cách tiếp tục, bạn đồng ý với
                        <a href="#" class="terms-link">Điều khoản dịch vụ</a>
                        của TodoList và xác nhận bạn đã đọc
                        <a href="#" class="terms-link">Chính sách quyền riêng tư</a>
                        của chúng tôi.
                    </p>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>