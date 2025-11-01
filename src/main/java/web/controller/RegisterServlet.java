package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.beans.User;
import web.dao.UserDAO;
import web.utils.ValidationUtil;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
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
            if (ValidationUtil.areAllNotEmpty(username, password, confirmPassword)){
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
                request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
                return;
            }

            //Check email
            if (!ValidationUtil.isNullOrEmpty(email) && !ValidationUtil.isValidEmail(email)) {
                request.setAttribute("error", "Địa chỉ email không hợp lệ");
                request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
                return;
            }

            //Check length of password
            if(!ValidationUtil.isValidPassword(password, 8)){
                request.setAttribute("error", "Mật khẩu phải có ít nhất 8 kì tự");
                request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
                return;
            }

            //Password match check
            if (!confirmPassword.equals(password)){
                request.setAttribute("error", "Mật khẩu không khớp");
                request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
                return;
            }

            // CHECK DATABASE AND PROCESS REGISTRATION
            UserDAO userDAO = new UserDAO();

            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null){
                request.setAttribute("error", "Người dùng đã tồn tại");
                request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
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
                request.setAttribute("error", "Đăng kí thất bại . Vui lòng thử lại");
                request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/auth/Register.jsp").forward(request, response);
        }
    }
}

