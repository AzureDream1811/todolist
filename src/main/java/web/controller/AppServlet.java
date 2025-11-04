package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.beans.Project;
import web.beans.Task;
import web.dao.ProjectDAO;
import web.dao.TaskDAO;
import web.beans.User;
import web.utils.WebUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/app/*")
public class AppServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();

    /**
     * Handles the GET request for the app page.
     * <p>
     * This method routes the request to the appropriate handler based on the path.
     * If the path is null or empty, it redirects the request to the inbox page.
     * If the path is not recognized, it redirects the request to the inbox page.
     *
     * @param request  the HttpServletRequest object containing the request parameters
     * @param response the HttpServletResponse object to send the response back to the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException      if an exception occurs during the input/output operations
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        // Get the path info (part after /app/)
        String pathInfo = request.getPathInfo();
        
        // Default to inbox if no path info
        if (pathInfo == null || pathInfo.equals("/")) {
            showInbox(request, response, currentUser);
            return;
        }
        
        // Remove leading slash and get the first part of the path
        String[] pathParts = pathInfo.substring(1).split("/");
        String action = pathParts[0];

        // Route to appropriate handler based on action
        switch (action) {
            case "tasks":
                request.getRequestDispatcher("/WEB-INF/views/app/AddTask.jsp").forward(request, response);
                break;
            case "projects":
                request.getRequestDispatcher("/WEB-INF/views/app/Projects.jsp").forward(request, response);
                break;
            case "inbox":
            default:
                showInbox(request, response, currentUser);
                break;
        }
    }

    private void showInbox(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        try {
            // pass tasks to jsp
            List<Task> tasks = taskDAO.getTasksByUserId(currentUser.getId());
            request.setAttribute("tasks", tasks);

            // pass projects to jsp
            List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
            request.setAttribute("projects", projects);

            // forward to inbox page
            request.getRequestDispatcher("/WEB-INF/views/app/Inbox.jsp").forward(request, response);
        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading inbox", "/app/inbox");
        }


    }
}
