package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.dao.UserDAO;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/auth/Login.jsp").forward(request, response);
    }

    /**
     * Handles the POST request for the login page.
     * <p>
     * This method retrieves the username and password from the request and checks if they are valid.
     * If the credentials are valid, it logs the user in and redirects them to the inbox page.
     * If the credentials are invalid, it sets an error message and forwards the request back to the login page.
     *
     * @param request the HttpServletRequest object containing the request parameters
     * @param response the HttpServletResponse object to send the response back to the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException if an exception occurs during the input/output operations
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        if (dao.authenticate(username, password)) {
            HttpSession session = request.getSession();

            session.setAttribute("currentUser", dao.getUserByUsername(username));
            response.sendRedirect(request.getContextPath() + "/app/inbox");

        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/auth/Login.jsp").forward(request, response);
        }
    }
}
