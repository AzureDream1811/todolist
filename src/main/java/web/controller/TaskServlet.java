package web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.beans.Task;
import web.beans.User;
import web.dao.TaskDAO;
import web.utils.ValidationUtils;
import web.utils.WebUtils;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/tasks", "/tasks/*"})
public class TaskServlet extends HttpServlet {
    private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.taskDAO = new TaskDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET requests (viewing task details)
        User user = WebUtils.validateAndGetUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();
        
        // If no specific task ID is provided, redirect to inbox
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/app/inbox");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Validate user
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");


        if (action == null || action.isEmpty()) {
            System.err.println("No action specified in request");
            WebUtils.sendError(request, response, "No action specified", "/app/inbox");
            return;
        }

        try {
            switch (action) {
                case "create":
                    handleCreateTask(request, response, currentUser);
                    break;
                default:
                    WebUtils.sendError(request, response, "Invalid action", "/app/inbox");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.sendError(request, response, "Error processing request: " + e.getMessage(), "/app/inbox");
        }
    }

    private void handleCreateTask(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            if (ValidationUtils.isNullOrEmpty(title)) {
                WebUtils.sendError(request, response, "Title is required", "/app/inbox");
                return;
            }
            
            // Handle optional fields with defaults
            int priority = 1; // default priority
            try {
                String priorityStr = request.getParameter("priority");
                if (priorityStr != null && !priorityStr.trim().isEmpty()) {
                    priority = Integer.parseInt(priorityStr);
                }
            } catch (NumberFormatException e) {
                // Use default priority on error
            }
            
            LocalDate dueDate = null;
            try {
                String dueDateStr = request.getParameter("dueDate");
                if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
                    dueDate = LocalDate.parse(dueDateStr);
                }
            } catch (Exception e) {
                // Leave as null if date is invalid
            }
            
            int projectId = 0; // default project ID
            try {
                String projectIdStr = request.getParameter("projectId");
                if (projectIdStr != null && !projectIdStr.trim().isEmpty()) {
                    projectId = Integer.parseInt(projectIdStr);
                }
            } catch (NumberFormatException e) {
                // Use default project ID on error
            }

            Task task = new Task(
                title.trim(),
                description != null ? description.trim() : "",
                false, // completed
                priority,
                dueDate,
                projectId,
                currentUser.getId()
            );
            
            boolean created = taskDAO.createTask(task);
            
            if (created) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/app/inbox"));
            } else {
                WebUtils.sendError(request, response, "Failed to create task. Please try again.", "/app/inbox");
            }
            
        } catch (Exception e) {
            WebUtils.sendError(request, response, "An error occurred while creating the task: " + e.getMessage(), "/app/inbox");
        }
    }


}
