package web.controller;

import com.oracle.wls.shaded.org.apache.regexp.RE;
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

@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet {
    private final String REGISTER_PAGE = "/WEB-INF/views/auth/Register.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        request.setAttribute("username", username);
        request.setAttribute("email", email);
        try {
            //Check Validation
            if (ValidationUtils.areAllNullOrEmpty(username, password, confirmPassword)){
                WebUtils.sendError(request, response, "Vui lòng điền đầy đủ thông tin", REGISTER_PAGE);
                return;
            }

            //Check email
            if (!ValidationUtils.isNullOrEmpty(email) && !ValidationUtils.isValidEmail(email)) {
                WebUtils.sendError(request, response, "Địa chỉ email không hợp lệ", REGISTER_PAGE);
                return;
            }

            //Check length of password
            if(!ValidationUtils.isValidPassword(password, 8)){
                WebUtils.sendError(request, response, "Mật khẩu phải có ít nhất 8 kì tự", REGISTER_PAGE);
                return;
            }

            //Password match check
            if (!confirmPassword.equals(password)){
                WebUtils.sendError(request, response, "Mật khẩu không khớp", REGISTER_PAGE);
                return;
            }

            // CHECK DATABASE AND PROCESS REGISTRATION
            UserDAO userDAO = new UserDAO();

            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null){
                WebUtils.sendError(request, response, "Người dùng đã tồn tại", REGISTER_PAGE);
                return;
            }

            User newUser = new User(username, password, email);
            User createdUser = userDAO.createUser(newUser);

            if (createdUser != null){
                HttpSession session = request.getSession();
                session.setAttribute("userName", username);
                session.setAttribute("user", createdUser);
                response.sendRedirect(request.getContextPath()+ "/app/inbox");
            }else {
                WebUtils.sendError(request, response, "Đăng kí thất bại . Vui lòng thử lại", REGISTER_PAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.sendError(request, response, "Lỗi hệ thống", REGISTER_PAGE);
        }
    }
}

