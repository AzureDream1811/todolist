package web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.dao.DAOFactory;
import web.dao.TaskDAO;
import web.dao.UserDAO;
import web.model.Task;
import web.model.User;
import web.utils.WebUtils;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private static final String ADMIN_USERS_PAGE = "/WEB-INF/views/admin/users.jsp";
    private static final String ADMIN_TASKS_PAGE = "/WEB-INF/views/admin/tasks.jsp";
    DAOFactory factory = DAOFactory.getInstance();
    UserDAO userDAO = factory.getUserDAO();
    TaskDAO taskDAO = factory.getTaskDAO();

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
            case "/":
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void getTasks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Task> tasks = taskDAO.getAllTasks();
        request.setAttribute("users", tasks);
        request.getRequestDispatcher(ADMIN_TASKS_PAGE).forward(request, response);
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher(ADMIN_USERS_PAGE).forward(request, response);
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

        switch (action) {
            case "users/promote":
                promoteUser(request);
                break;
            case "users/demote":
                promoteUser(request);
                break;
            case "users/delete":
                deleteUser(request);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }

    }

    private void deleteUser(HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    private void promoteUser(HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'promoteUser'");
    }

}
