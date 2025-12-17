package web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.dao.DAOFactory;
import web.dao.ProjectDAO;
import web.dao.TaskDAO;
import web.dao.UserDAO;
import web.model.Project;
import web.model.Task;
import web.model.User;
import web.utils.WebUtils;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private static final String ADMIN_USERS_PAGE = "/WEB-INF/views/admin/Users.jsp";
    private static final String ADMIN_TASKS_PAGE = "/WEB-INF/views/admin/Tasks.jsp";
    private static final String ADMIN_PROJECTS_PAGE = "/WEB-INF/views/admin/Projects.jsp";
    DAOFactory factory = DAOFactory.getInstance();
    UserDAO userDAO = factory.getUserDAO();
    TaskDAO taskDAO = factory.getTaskDAO();
    ProjectDAO projectDAO = factory.getProjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User admin = WebUtils.validateAdminAndGetUser(request, response);
        if (admin == null) {
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = (pathInfo == null || pathInfo.equals("/")) ? "/" : pathInfo;

        switch (action) {
            case "/users":
                getUsers(request, response);
                break;

            case "/tasks":
                getTasks(request, response);
                break;

            case "/projects":
                getProjects(request, response);
                break;

            case "/":
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    /**
     * Handles the GET request for the users page.
     * <p>
     * This method retrieves all users from the database and passes them to the
     * jsp.
     * If an exception occurs during the processing, it sends an error response to
     * the client.
     *
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back to
     *                 the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException      if an exception occurs during the input/output
     *                          operations
     */
    private void getUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher(ADMIN_USERS_PAGE).forward(request, response);
    }

    /**
     * Handles the GET request for the tasks page.
     * <p>
     * This method retrieves all tasks from the database and passes them to the
     * jsp.
     * If an exception occurs during the processing, it sends an error response to
     * the client.
     *
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back to
     *                 the client
     * @throws ServletException if an exception occurs during the servlet processing
     * @throws IOException      if an exception occurs during the input/output
     *                          operations
     */
    private void getTasks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Task> tasks = taskDAO.getAllTasks();
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher(ADMIN_TASKS_PAGE).forward(request, response);
    }

    private void getProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Project> projects = projectDAO.getAllProjects();
        request.setAttribute("projects", projects);
        request.getRequestDispatcher(ADMIN_PROJECTS_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User admin = WebUtils.validateAdminAndGetUser(request, response);
        if (admin == null) {
            return;
        }
        String pathInfo = request.getPathInfo();
        String action = (pathInfo == null || pathInfo.equals("/")) ? "/" : pathInfo;

        // Remove leading slash if present for consistent matching
        if (action.startsWith("/")) {
            action = action.substring(1);
        }

        switch (action) {
            case "users/promote":
                promoteUser(request);
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;

            case "users/demote":
                demoteUser(request);
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;

            case "users/delete":
                deleteUser(request);
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;

            case "tasks/delete":
                deleteTask(request);
                response.sendRedirect(request.getContextPath() + "/admin/tasks");
                break;
            case "projects/delete":
                deleteProject(request);
                response.sendRedirect(request.getContextPath() + "/admin/projects");
                break;

            default:
                System.out.println("Action not found: " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }

    }

    private void deleteTask(HttpServletRequest request) {
        String taskIdStr = request.getParameter("taskId");
        if (taskIdStr == null || taskIdStr.isBlank()) {
            request.setAttribute("error", "missing taskId");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdStr);
            taskDAO.deleteTaskById(taskId);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "invalid task ID");
        } catch (Exception e) {
            request.setAttribute("error", "failed to delete task");
        }
    }

    private void deleteProject(HttpServletRequest request) {
        String projectIdStr = request.getParameter("projectId");
        if (projectIdStr == null || projectIdStr.isBlank()) {
            request.setAttribute("error", "missing project ID");
            return;
        }

        try {
            int projectId = Integer.parseInt(projectIdStr);
            projectDAO.deleteProjectById(projectId);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "invalid project ID");
        } catch (Exception e) {
            request.setAttribute("error", "failed to delete project");
        }
    }

    private void deleteUser(HttpServletRequest request) {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.isBlank()) {
            request.setAttribute("error", "missing user ID");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);
            if (user == null) {
                request.setAttribute("error", "user not found");
                return;
            }

            if (user.getRole().equalsIgnoreCase("admin")) {
                request.setAttribute("error", "cannot delete admin");
                return;
            }

            userDAO.deleteUserById(userId);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "invalid user ID");
        } catch (Exception e) {
            request.setAttribute("error", "failed to delete user");
        }
    }

    private void demoteUser(HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'demoteUser'");
    }

    private void promoteUser(HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'promoteUser'");
    }

}