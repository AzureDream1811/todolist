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
     * Handles the GET request for the add task page.
     * <p>
     * This method validates the user, fetches all projects of the current user, and
     * passes them to the jsp. If an exception occurs during the processing, it
     * sends an error response to the client.
     *
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back to
     *                 the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException      if an exception occurs during the input/output
     *                          operations
     */
    private void showAddTaskForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null)
            return;

        try {
            List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
            request.setAttribute("projects", projects);
            request.getRequestDispatcher(ADD_TASK_PAGE).forward(request, response);
        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading add task", "/app/inbox");
        }
    }

    /**
     * Handles the add task request.
     * <p>
     * This method validates the user, extracts the task parameters from the
     * request,
     * creates a new task with the given parameters, and adds it to the database.
     * It also sends an email notification to the user if the user's email is not
     * null.
     * Finally, it redirects the user to the appropriate page based on the task
     * type.
     * 
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back to
     *                 the client
     * @throws IOException      if an exception occurs during the input/output
     *                          operations
     * @throws ServletException if an exception occurs during the servlet processing
     */
    private void handleAddTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null)
            return;

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
            String dueDateStr = request.getParameter("dueDate");
            String priorityParam = request.getParameter("priority");
            String projectIdParam = request.getParameter("projectId");
            String taskType = request.getParameter("taskType");
            String redirectProjectIdParam = request.getParameter("redirectProjectId");

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
            int projectId = (projectIdParam != null && !projectIdParam.isEmpty()) ? Integer.parseInt(projectIdParam)
                    : 0;
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

            // Use redirectProjectId for redirect (to stay on the same project page)
            int redirectProjectId = (redirectProjectIdParam != null && !redirectProjectIdParam.isEmpty()) 
                    ? Integer.parseInt(redirectProjectIdParam) : projectId;
            String redirectPath = determineRedirectPath(taskType, redirectProjectId);
            response.sendRedirect(request.getContextPath() + redirectPath);
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.sendError(request, response, "Error: " + e.getMessage(), "/app/inbox");
        }
    }

    /**
     * Determines the redirect path given the task type.
     * <p>
     * Given the task type parameter, this method returns the corresponding redirect
     * path
     * to the corresponding page. If the task type is null or empty, it returns the
     * path
     * to the inbox page. If the task type is "today", it returns the path to the
     * today
     * page. If the task type is "upcoming", it returns the path to the upcoming
     * page. If the task type is "project", it returns the path to the project
     * detail page. If the task type is anything else, it returns the path to the
     * inbox page.
     * 
     * @param taskType the task type to determine the redirect path for
     * @param projectId the project ID to redirect to (used when taskType is "project")
     * @return the redirect path to the corresponding page
     */
    private String determineRedirectPath(String taskType, int projectId) {
        if (taskType == null || taskType.isEmpty()) {
            return "/app/inbox";
        }
        switch (taskType) {
            case "today":
                return "/app/today";
            case "upcoming":
                return "/app/upcoming";
            case "project":
                return "/app/projects?id=" + projectId;
            case "inbox":
            default:
                return "/app/inbox";
        }
    }

    /**
     * Completes a task given its ID.
     * <p>
     * This method takes the task ID parameter from the request and completes the
     * task
     * with the given ID from the database. If the task ID is null or empty, it
     * sends an error response to the client. If the task ID is invalid, it sends
     * a bad request response to the client. If an exception occurs during the
     * completion, it sends a bad request response to the client.
     * <p>
     * Security check: Only complete task if it belongs to the user
     * 
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back
     *                 to the client
     * @throws IOException if an exception occurs during the input/output
     *                     operations
     */
    private void completeTask(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null)
            return;

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

    /**
     * Deletes a task from the database given its ID.
     * <p>
     * This method takes the task ID parameter from the request and deletes the task
     * with the given ID from the database. If the task ID is null or empty, it
     * sends a 400 Bad Request response to the client. If the task ID is invalid, it
     * sends a 400 Bad Request response to the client. If an exception occurs during
     * the deletion, it sends a 400 Bad Request response to the client.
     * <p>
     * Security check: Only delete task if it belongs to the user
     * 
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back
     *                 to the client
     * @throws IOException if an exception occurs during the input/output
     *                     operations
     */
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
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back to
     *                 the client
     * @throws IOException if an exception occurs during the input/output operations
     */
    private void updateTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null)
            return;

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
                            "<p>You have updated the task: <b>" + existingTask.getTitle() + "</b></p>");
        }

        // Check if this is an AJAX request
        String isAjax = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(isAjax) || request.getContentType() != null
                && request.getContentType().contains("application/x-www-form-urlencoded")) {
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