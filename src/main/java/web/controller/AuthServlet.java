package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.beans.User;
import web.dao.UserDAO;
import web.utils.ValidationUtils;
import web.utils.WebUtils;

import java.io.IOException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private static final String LOGIN_PAGE = "/WEB-INF/views/auth/Login.jsp";
    private static final String REGISTER_PAGE = "/WEB-INF/views/auth/Register.jsp";
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = WebUtils.getActionFormPath(request, response);
        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        switch (action) {
            case "login":
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                break;
            case "register":
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
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

    private void registerHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        request.setAttribute("username", username);
        request.setAttribute("email", email);
        try {
            //Check Validation
            if (ValidationUtils.areAllNullOrEmpty(username, password, confirmPassword)) {
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            //Check email
            if (!ValidationUtils.isNullOrEmpty(email) && !ValidationUtils.isValidEmail(email)) {
                request.setAttribute("error", "Địa chỉ email không hợp lệ");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            //Check length of password
            if (!ValidationUtils.isValidPassword(password, 8)) {
                request.setAttribute("error", "Mật khẩu phải có ít nhất 8 kì tự");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            //Password match check
            if (!confirmPassword.equals(password)) {
                request.setAttribute("error", "Mật khẩu không khớp");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            // CHECK DATABASE AND PROCESS REGISTRATION
            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null) {
                request.setAttribute("error", "Người dùng đã tồn tại");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            User newUser = new User(username, password, email);
            User createdUser = userDAO.createUser(newUser);

            if (createdUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userName", username);
                session.setAttribute("user", createdUser);
                response.sendRedirect(request.getContextPath() + "/app/inbox");
            } else {
                request.setAttribute("error", "Đăng kí thất bại . Vui lòng thử lại");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            }
        } catch (Exception e) {
                WebUtils.sendError(request, response, "Lỗi hệ thống", REGISTER_PAGE);

        }

    }

    private void loginHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (userDAO.authenticate(username, password)) {
            HttpSession session = request.getSession();

            session.setAttribute("currentUser", userDAO.getUserByUsername(username));
            response.sendRedirect(request.getContextPath() + "/app/inbox");

        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        }
    }
}
