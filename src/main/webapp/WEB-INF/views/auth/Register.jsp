<%--
  Created by IntelliJ IDEA.
  User: Hi
  Date: 01/11/2025
  Time: 1:06 SA
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Đăng ký - TodoList</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/auth.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth/register.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-container">
        <div class="auth-form-container">

            <c:if test="${not empty requestScope.error}">
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i>
                        ${requestScope.error}
                </div>
            </c:if>

            <%--logo--%>
            <div class="logo-section">
                <div class="logo-circle">
                    <i class="fas fa-check-circle logo-icon" id="todo-logo"></i>
                </div>
                <h2 class="welcome-title">Tạo tài khoản TodoList</h2>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/auth/register" class="register-form">

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text"
                               name="username"
                               id="username"
                               class="form-input"
                               placeholder="Tên đăng nhập"
                               value="${param.username}"
                               required>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="email"
                               name="email"
                               id="email"
                               class="form-input"
                               placeholder="your@email.com"
                               value="${param.email}"
                               required>
                    </div>
                </div>

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

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password"
                               name="confirmPassword"
                               id="confirmPassword"
                               class="form-input"
                               placeholder="Nhập lại mật khẩu"
                               required>
                    </div>
                </div>

                <div class="login-prompt">
                    <a href="${pageContext.request.contextPath}/auth/login" class="login-link-prompt">
                        Đã có tài khoản? Đăng nhập ngay
                    </a>
                </div>

                <div class="divider"></div>

                <div class="button-section">
                    <button type="submit" class="btn btn-register-main">
                        Đăng ký
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
