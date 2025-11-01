package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.beans.Task;
import web.dao.TaskDAO;
import web.beans.User;
import web.utils.WebUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/app/*")
public class AppServlet extends HttpServlet {
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

        User currentUser = WebUtils.validateAndGetUser(request,response);
        if (currentUser == null) return;

        String action = WebUtils.getActionFormPath(request,response);
        if (action == null) return;

        // Route to appropriate handler based on action
        switch (action) {
            case "projects":
                request.getRequestDispatcher("/WEB-INF/app/Projects.jsp").forward(request, response);
                break;
            case "inbox":
            default:
                showInbox(request, response, currentUser);
                break;
        }
    }

    private void showInbox(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        try {
            TaskDAO taskDAO = new TaskDAO();
            List<Task> tasks = taskDAO.getTasksByUserId(currentUser.getId());
            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("/WEB-INF/app/Inbox.jsp").forward(request, response);

            response.sendRedirect(request.getContextPath() + "/app/inbox");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/app/Add-task.jsp").forward(request, response);
        }


    }
}
