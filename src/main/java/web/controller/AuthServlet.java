package web.controller;

import web.model.Task;
import web.model.User;
import web.dao.DAOFactory;
import web.dao.TaskDAO;
import web.dao.UserDAO;
import web.utils.EmailUtils;
import web.utils.ValidationUtils;
import web.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
  private static final String LOGIN_PAGE = "/WEB-INF/views/auth/Login.jsp";
  private static final String REGISTER_PAGE = "/WEB-INF/views/auth/Register.jsp";
  private static final String FORGOT_PASSWORD_PAGE = "/WEB-INF/views/auth/ForgotPassword.jsp";
  private static final String RESET_PASSWORD_PAGE = "/WEB-INF/views/auth/ResetPassword.jsp";
  private final DAOFactory factory = DAOFactory.getInstance();
  private final UserDAO userDAO = factory.getUserDAO();

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
      case "/forgot-password":
        request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
        break;
      case "/reset-password":
        showResetPasswordPage(request, response);
        break;
      default:
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        break;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

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
      case "forgot-password":
        forgotPasswordHandler(request, response);
        break;
      case "reset-password":
        resetPasswordHandler(request, response);
        break;
      default:
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        break;
    }
  }
  /**
 * Handles the registration process. This method validates the input parameters,
 * checks for the existence of the user in the database, and processes the
 * registration if the user does not exist. If the registration is successful,
            * it redirects the user to the inbox page.
            * * Additionally, this method performs the following validations:
            * - Checks if required fields (username, password) are empty.
            * - Validates the email format.
            * - Ensures the password meets the minimum length (8 characters).
            * - Verifies that the password and confirmation password match.
            * * Upon successful registration, a welcome email is sent asynchronously to the
 * user's registered email address.
            * * If the registration fails at any step, it redirects the user back to the
 * registration page with an error message.
            * * @param request  the HttpServletRequest object containing the request
 * parameters
 * @param response the HttpServletResponse object to send the response back to
 * the client
 * @throws ServletException if an exception occurs during the servlet processing
 * @throws IOException      if an exception occurs during the input/output
 * operations
 */
    private void registerHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        request.setAttribute("username", username);
        request.setAttribute("email", email);

        try {
            if (ValidationUtils.areAllNullOrEmpty(username, password, confirmPassword)) {
                WebUtils.sendError(request, response, "Please fill in all the information", REGISTER_PAGE);
                return;
            }

            if (!ValidationUtils.isNullOrEmpty(email) && !ValidationUtils.isValidEmail(email)) {
                WebUtils.sendError(request, response, "Invalid email address", REGISTER_PAGE);
                return;
            }

            if (!ValidationUtils.isValidPassword(password, 8)) {
                WebUtils.sendError(request, response, "The password must have at least 8 characters", REGISTER_PAGE);
                return;
            }

            if (!confirmPassword.equals(password)) {
                WebUtils.sendError(request, response, "Password doesn't match", REGISTER_PAGE);
                return;
            }

            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null) {
                WebUtils.sendError(request, response, "The user already exists", REGISTER_PAGE);
                return;
            }

            User newUser = new User(username, password, email);
            User createdUser = userDAO.createUser(newUser);

            if (createdUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", createdUser);

                String subject = "Welcome to TodoList!";
                String content = "<h2>Congratulations " + username + "!</h2>"
                        + "<p>You have successfully registered an account. Start managing your tasks now!</p>";
                web.utils.EmailUtils.sendEmailAsync(email, subject, content);

                response.sendRedirect(request.getContextPath() + "/app/inbox");
            } else {
                WebUtils.sendError(request, response, "Registration failed. Please try again", REGISTER_PAGE);
            }
        } catch (Exception e) {
            WebUtils.sendError(request, response, "System error", REGISTER_PAGE);
        }
    }

    /**
     * Handles the login request.
     * * This method authenticates the user credentials and logs the user in if
     * the credentials are valid. If the user is an admin, it redirects
     * the user to the admin page; otherwise, it redirects the user to the inbox
     * page.
     * * Additionally, if the user has a registered email, this method checks for
     * overdue tasks, today's tasks, and upcoming tasks for tomorrow to send
     * a reminder email.
     * * If the credentials are invalid, it sends an error response to the
     * client.
     * * @param request  the HttpServletRequest object containing the request
     * parameters
     * @param response the HttpServletResponse object to send the response back to
     * the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException      if an exception occurs during the input/output
     * operations
     */
    private void loginHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userDAO.authenticate(username, password)) {
            User user = userDAO.getUserByUsername(username);
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                TaskDAO taskDAO = factory.getTaskDAO();
                LocalDate today = java.time.LocalDate.now();
                List<Task> overdue = taskDAO.getOverdueTasksByUserId(user.getId());
                List<Task> todayTasks = taskDAO.getTodayTasksByUserId(user.getId());
                List<Task> tomorrow = taskDAO.getTasksDueOn(user.getId(), today.plusDays(1));
                if (!overdue.isEmpty() || !todayTasks.isEmpty() || !tomorrow.isEmpty()) {
                    web.utils.EmailUtils.sendTaskReminder(user, overdue, todayTasks, tomorrow);
                }
            }
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                response.sendRedirect(request.getContextPath() + "/app/inbox");
            }
        } else {
            WebUtils.sendError(request, response, "Invalid username or password", LOGIN_PAGE);
        }
    }

  /**
   * Handles the logout request.
   * <p>
   * This method invalidates the user session and prevents caching after logout to
   * prevent going back with the back button.
   * It then redirects the request to the login page.
   * <p>
   * 
   * @param request  the HttpServletRequest object containing the request
   *                 parameters
   * @param response the HttpServletResponse object to send the response back to
   *                 the client
   * @throws IOException if an exception occurs during the input/output operations
   */
  private void logoutHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

  /**
   * Shows the reset password page.
   * Validates the token from the URL and displays the form if valid.
   */
  private void showResetPasswordPage(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String token = request.getParameter("token");
    
    if (token == null || token.isEmpty()) {
      request.setAttribute("invalidToken", true);
      request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
      return;
    }

    User user = userDAO.getUserByResetToken(token);
    if (user == null) {
      request.setAttribute("invalidToken", true);
    } else {
      request.setAttribute("token", token);
      request.setAttribute("invalidToken", false);
    }
    
    request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
  }

  /**
   * Handles the forgot password request.
   * Generates a reset token, saves it to the database, and sends an email with the reset link.
   */
  private void forgotPasswordHandler(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String email = request.getParameter("email");
    request.setAttribute("email", email);

    if (email == null || email.trim().isEmpty()) {
      request.setAttribute("error", "Vui lòng nhập email");
      request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
      return;
    }

    if (!ValidationUtils.isValidEmail(email)) {
      request.setAttribute("error", "Email không hợp lệ");
      request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
      return;
    }

    User user = userDAO.getUserByEmail(email.trim());
    
    // Always show success message to prevent email enumeration attacks
    if (user != null) {
      // Generate a unique token
      String token = UUID.randomUUID().toString();
      
      // Save token to database
      userDAO.saveResetToken(user.getId(), token);
      
      // Build reset link
      String resetLink = request.getScheme() + "://" + request.getServerName() 
              + ":" + request.getServerPort() + request.getContextPath() 
              + "/auth/reset-password?token=" + token;
      
      // Send email
      String subject = "TodoList - Đặt lại mật khẩu";
      String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
              + "<h2 style='color: #8b5cf6;'>Đặt lại mật khẩu</h2>"
              + "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>"
              + "<p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản TodoList của mình.</p>"
              + "<p>Nhấn vào nút bên dưới để đặt lại mật khẩu:</p>"
              + "<div style='text-align: center; margin: 30px 0;'>"
              + "<a href='" + resetLink + "' style='background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%); "
              + "color: white; padding: 12px 30px; text-decoration: none; border-radius: 25px; font-weight: bold;'>"
              + "Đặt lại mật khẩu</a></div>"
              + "<p style='color: #666; font-size: 14px;'>Link này sẽ hết hạn sau 1 giờ.</p>"
              + "<p style='color: #666; font-size: 14px;'>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>"
              + "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>"
              + "<p style='color: #999; font-size: 12px;'>Email này được gửi tự động từ TodoList.</p>"
              + "</div>";
      
      EmailUtils.sendEmailAsync(email, subject, content);
    }
    
    // Always show success to prevent email enumeration
    request.setAttribute("successMessage", "Nếu email tồn tại trong hệ thống, bạn sẽ nhận được link đặt lại mật khẩu.");
    request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
  }

  /**
   * Handles the reset password request.
   * Validates the token, updates the password, and clears the token.
   */
  private void resetPasswordHandler(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String token = request.getParameter("token");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");

    if (token == null || token.isEmpty()) {
      request.setAttribute("invalidToken", true);
      request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
      return;
    }

    request.setAttribute("token", token);

    // Validate password
    if (password == null || password.isEmpty()) {
      request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
      request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
      return;
    }

    if (!ValidationUtils.isValidPassword(password, 8)) {
      request.setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự");
      request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
      return;
    }

    if (!password.equals(confirmPassword)) {
      request.setAttribute("error", "Mật khẩu xác nhận không khớp");
      request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
      return;
    }

    // Validate token and get user
    User user = userDAO.getUserByResetToken(token);
    if (user == null) {
      request.setAttribute("invalidToken", true);
      request.getRequestDispatcher(RESET_PASSWORD_PAGE).forward(request, response);
      return;
    }

    // Update password
    userDAO.updatePassword(user.getId(), password);
    
    // Clear the reset token
    userDAO.clearResetToken(user.getId());

    // Send confirmation email
    String subject = "TodoList - Mật khẩu đã được thay đổi";
    String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
            + "<h2 style='color: #8b5cf6;'>Mật khẩu đã được thay đổi</h2>"
            + "<p>Xin chào <b>" + user.getUsername() + "</b>,</p>"
            + "<p>Mật khẩu của bạn đã được thay đổi thành công.</p>"
            + "<p>Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ với chúng tôi ngay.</p>"
            + "<div style='text-align: center; margin: 30px 0;'>"
            + "<a href='" + request.getScheme() + "://" + request.getServerName() 
            + ":" + request.getServerPort() + request.getContextPath() + "/auth/login' "
            + "style='background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%); "
            + "color: white; padding: 12px 30px; text-decoration: none; border-radius: 25px; font-weight: bold;'>"
            + "Đăng nhập ngay</a></div>"
            + "</div>";
    
    if (user.getEmail() != null) {
      EmailUtils.sendEmailAsync(user.getEmail(), subject, content);
    }

    // Redirect to login with success message
    request.getSession().setAttribute("successMessage", "Mật khẩu đã được đặt lại thành công! Vui lòng đăng nhập.");
    response.sendRedirect(request.getContextPath() + "/auth/login");
  }
}