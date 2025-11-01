package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import web.beans.Task;
import web.dao.TaskDAO;
import web.beans.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/app/*")
public class AppServlet extends HttpServlet {
    /**
     * Handles the GET request for the app page.
     * <p>
     * This method checks if the user is logged in and redirects them to the inbox page if they are not.
     * If the user is logged in, it checks if the path is null or equals "/" and redirects them to the inbox page if so.
     * If the path is not null or does not equal "/", it checks if the path equals "projects" and forwards the request to the projects page if so.
     * If the path is not "projects", it retrieves the tasks for the current user and forwards the request to the inbox page.
     *
     * @param request the HttpServletRequest object containing the request parameters
     * @param response the HttpServletResponse object to send the response back to the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException if an exception occurs during the input/output operations
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        
        // Handle root path
        if (path == null || path.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/app/inbox");
            return;
        }
        
        String action = path.substring(1);
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Redirect to log in if user is not logged in
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (action) {
            case "projects":
                request.getRequestDispatcher("/WEB-INF/app/Projects.jsp").forward(request, response);
                break;
            case "inbox":
            default:
                // Get tasks for the current user
                TaskDAO taskDAO = new TaskDAO();
                List<Task> tasks = taskDAO.getTasksByUserId(currentUser.getId());
                request.setAttribute("tasks", tasks);
                request.getRequestDispatcher("/WEB-INF/app/Inbox.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
