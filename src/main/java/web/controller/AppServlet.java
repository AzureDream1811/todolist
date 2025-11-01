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
