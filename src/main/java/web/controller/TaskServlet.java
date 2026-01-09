package web.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.dao.DAOFactory;
import web.dao.ProjectDAO;
import web.dao.TaskDAO;
import web.model.Project;
import web.model.Task;
import web.model.User;
import web.utils.EmailUtils;
import web.utils.WebUtils;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {
    private static final String ADD_TASK_PAGE = "/WEB-INF/views/component/AddTask.jsp";
    
    private final DAOFactory factory = DAOFactory.getInstance();
    private final TaskDAO taskDAO = factory.getTaskDAO();
    private final ProjectDAO projectDAO = factory.getProjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        
        if (path == null || "/".equals(path)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/add":
                showAddTaskForm(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();
        
        if (path == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/add":
                handleAddTask(request, response);
                break;
            case "/update":
                updateTask(request, response);
                break;
            case "/delete":
                deleteTask(request, response);
                break;
            case "/complete":
                completeTask(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Shows the add task form page.
     */
    private void showAddTaskForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        try {
            List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
            request.setAttribute("projects", projects);
            request.getRequestDispatcher(ADD_TASK_PAGE).forward(request, response);
        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading add task", "/app/inbox");
        }
    }

    /**
     * Handles the POST request to add a new task.
     */
    private void handleAddTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
            String dueDateStr = request.getParameter("dueDate");
            String priorityParam = request.getParameter("priority");
            String projectIdParam = request.getParameter("projectId");
            String taskType = request.getParameter("taskType");

            Task task = new Task();
            task.setTitle(title);
            task.setDescription((description == null || description.trim().isEmpty()) ? null : description);
            task.setCompletedAt(completed ? LocalDate.now() : null);

            // Handle due date
            LocalDate finalDueDate;
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                finalDueDate = LocalDate.parse(dueDateStr);
            } else {
                finalDueDate = LocalDate.now();
            }
            task.setDueDate(finalDueDate);

            // Handle priority (1-3)
            int priority;
            try {
                priority = (priorityParam != null && !priorityParam.isEmpty()) ? Integer.parseInt(priorityParam) : 3;
                if (priority < 1 || priority > 3) {
                    priority = 3;
                }
            } catch (NumberFormatException e) {
                priority = 3;
            }
            task.setPriority(priority);

            // Handle project ID
            int projectId = (projectIdParam != null && !projectIdParam.isEmpty()) ? Integer.parseInt(projectIdParam) : 0;
            task.setProjectIdObject(projectId > 0 ? projectId : null);

            task.setUserId(currentUser.getId());

            taskDAO.createTask(task);

            // Send email notification
            if (currentUser.getEmail() != null) {
                String mailContent = "<h3>New Task Added!</h3>" +
                        "<p>Title: <b>" + title + "</b></p>" +
                        "<p>Priority: <b>" + priority + "</b></p>" +
                        "<p>Due date: <b>" + finalDueDate + "</b></p>";
                EmailUtils.sendEmailAsync(currentUser.getEmail(), "Task Notification", mailContent);
            }

            String redirectPath = determineRedirectPath(taskType);
            response.sendRedirect(request.getContextPath() + redirectPath);
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.sendError(request, response, "Error: " + e.getMessage(), "/app/inbox");
        }
    }

    /**
     * Determines the redirect path based on the task type.
     */
    private String determineRedirectPath(String taskType) {
        if (taskType == null || taskType.isEmpty()) {
            return "/app/inbox";
        }
        switch (taskType) {
            case "today":
                return "/app/today";
            case "upcoming":
                return "/app/upcoming";
            case "inbox":
            default:
                return "/app/inbox";
        }
    }

    /**
     * Handles the complete task request.
     */
    private void completeTask(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        String taskIdStr = request.getParameter("taskId");
        if (taskIdStr != null) {
            try {
                int taskId = Integer.parseInt(taskIdStr);
                // Security check
                Task task = taskDAO.getTaskById(taskId);
                if (task != null && task.getUserId() == currentUser.getId()) {
                    taskDAO.completeTask(taskId);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = WebUtils.validateAndGetUser(request, response);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String taskIdStr = request.getParameter("taskId");

        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdStr);

            // Security check: Chỉ xóa task thuộc về user
            Task task = taskDAO.getTaskById(taskId);
            if (task != null && task.getUserId() == user.getId()) {
                taskDAO.deleteTaskById(taskId);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



/**
 * Updates a task in the database based on the given parameters.
 * Supports both full page form submissions and AJAX requests.
 *
 * @param request     the HttpServletRequest object containing the request parameters
 * @param response    the HttpServletResponse object to send the response back to the client
 * @throws IOException if an exception occurs during the input/output operations
 */
private void updateTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User currentUser = WebUtils.validateAndGetUser(request, response);
    if (currentUser == null) return;

    String taskIdStr = request.getParameter("id");
    if (taskIdStr == null || taskIdStr.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }

    int taskId = Integer.parseInt(taskIdStr);
    
    // Security check: Only update task if it belongs to the user
    Task existingTask = taskDAO.getTaskById(taskId);
    if (existingTask == null || existingTask.getUserId() != currentUser.getId()) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }

    // Update task with provided values, keep existing values if not provided
    String title = request.getParameter("title");
    if (title != null && !title.isEmpty()) {
        existingTask.setTitle(title);
    }

    String description = request.getParameter("description");
    if (description != null) {
        existingTask.setDescription(description.isEmpty() ? null : description);
    }

    String priorityStr = request.getParameter("priority");
    if (priorityStr != null && !priorityStr.isEmpty()) {
        int priority = Integer.parseInt(priorityStr);
        if (priority >= 1 && priority <= 3) {
            existingTask.setPriority(priority);
        }
    }

    String dueDateStr = request.getParameter("dueDate");
    if (dueDateStr != null && !dueDateStr.isEmpty()) {
        existingTask.setDueDate(LocalDate.parse(dueDateStr));
    }

    String completedAtStr = request.getParameter("completedAt");
    if (completedAtStr != null && !completedAtStr.isEmpty()) {
        existingTask.setCompletedAt(LocalDate.parse(completedAtStr));
    }

    String projectIdStr = request.getParameter("projectId");
    if (projectIdStr != null) {
        if (projectIdStr.isEmpty() || "0".equals(projectIdStr)) {
            existingTask.setProjectIdObject(null);
        } else {
            existingTask.setProjectIdObject(Integer.parseInt(projectIdStr));
        }
    }

    taskDAO.updateTask(existingTask);

    // Send email notification
    if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
        EmailUtils.sendEmailAsync(
                currentUser.getEmail(),
                "Task updated successfully",
                "<h3>TodoList Notification</h3>" +
                        "<p>You have updated the task: <b>" + existingTask.getTitle() + "</b></p>"
        );
    }

    // Check if this is an AJAX request
    String isAjax = request.getHeader("X-Requested-With");
    if ("XMLHttpRequest".equals(isAjax) || request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
        response.setStatus(HttpServletResponse.SC_OK);
    } else {
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isEmpty()) {
            response.sendRedirect(request.getContextPath() + redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/inbox");
        }
    }
}
}