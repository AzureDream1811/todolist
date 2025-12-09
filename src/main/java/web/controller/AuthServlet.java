package web.controller;


import web.model.User;
import web.dao.studentDAOImpl.UserDAOImpl;
import web.utils.ValidationUtils;
import web.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
  private static final String LOGIN_PAGE = "/WEB-INF/views/auth/Login.jsp";
  private static final String REGISTER_PAGE = "/WEB-INF/views/auth/Register.jsp";
  private final UserDAOImpl userDAOImpl = new UserDAOImpl();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String pathInfo = request.getPathInfo();
    String action = (pathInfo == null || pathInfo.equals("/")) ? "/login" : pathInfo;

    switch (action) {
      case "/login":
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        break;
      case "/register":
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
        break;
      case "/logout":
        logoutHandler(request, response);
        break;
      default:
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        break;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = WebUtils.getActionFormPath(request, response);

    if (action == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    switch (action) {
      case "login":
        loginHandler(request, response);
        break;
      case "register":
        registerHandler(request, response);
        break;
      default:
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        break;
    }
  }

  private void registerHandler(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    String email = request.getParameter("email");

    request.setAttribute("username", username);
    request.setAttribute("email", email);
    try {
      // Check Validation
      if (ValidationUtils.areAllNullOrEmpty(username, password, confirmPassword)) {
        WebUtils.sendError(request, response, "Vui lòng điền đầy đủ thông tin", REGISTER_PAGE);
        return;
      }

      // Check email
      if (!ValidationUtils.isNullOrEmpty(email) && !ValidationUtils.isValidEmail(email)) {
        WebUtils.sendError(request, response, "Địa chỉ email không hợp lệ", REGISTER_PAGE);
        return;
      }

      // Check length of password
      if (!ValidationUtils.isValidPassword(password, 8)) {
        WebUtils.sendError(request, response, "Mật khẩu phải có ít nhất 8 kì tự", REGISTER_PAGE);
        return;
      }

      // Password match check
      if (!confirmPassword.equals(password)) {
        WebUtils.sendError(request, response, "Mật khẩu không khớp", REGISTER_PAGE);
        return;
      }

      // CHECK DATABASE AND PROCESS REGISTRATION
      User existingUser = userDAOImpl.getUserByUsername(username);
      if (existingUser != null) {
        WebUtils.sendError(request, response, "Người dùng đã tồn tại", REGISTER_PAGE);
        return;
      }

      User newUser = new User(username, password, email);
      User createdUser = userDAOImpl.createUser(newUser);

      if (createdUser != null) {
        HttpSession session = request.getSession();
        session.setAttribute("userName", username);
        session.setAttribute("user", createdUser);
        response.sendRedirect(request.getContextPath() + "/app/inbox");
      } else {
        WebUtils.sendError(request, response, "Đăng kí thất bại . Vui lòng thử lại", REGISTER_PAGE);
      }
    } catch (Exception e) {
      WebUtils.sendError(request, response, "Lỗi hệ thống", REGISTER_PAGE);

    }

  }

  private void loginHandler(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (userDAOImpl.authenticate(username, password)) {
      HttpSession session = request.getSession();

      session.setAttribute("currentUser", userDAOImpl.getUserByUsername(username));
      response.sendRedirect(request.getContextPath() + "/app/inbox");

    } else {
      WebUtils.sendError(request, response, "Invalid username or password", LOGIN_PAGE);
    }
  }

  private void logoutHandler(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    // prevent caching after logout to prevent going back with back button
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    try {
      response.sendRedirect(request.getContextPath() + "/auth/login");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}