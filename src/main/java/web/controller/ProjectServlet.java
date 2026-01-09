package web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.dao.DAOFactory;
import web.dao.ProjectDAO;
import web.model.Project;
import web.model.User;
import web.utils.WebUtils;

@WebServlet("/projects/*")
public class ProjectServlet extends HttpServlet {
    DAOFactory daoFactory = DAOFactory.getInstance();
    ProjectDAO projectDAO = daoFactory.getProjectDAO();
    private static final String PROJECTS_PAGE = "/WEB-INF/views/app/Projects.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || "/".equals(path)) {
            User user = WebUtils.validateAndGetUser(request, response);
            if (user == null)
                return;

            request.setAttribute("projects", projectDAO.getProjectsByUserId(user.getId()));
            request.getRequestDispatcher(PROJECTS_PAGE).forward(request, response);
            return;
        }

        if ("/add".equals(path)) {
            request.getRequestDispatcher("/addProject.jsp").forward(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/add":
                handleAddProject(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private void handleAddProject(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        User user = WebUtils.validateAndGetUser(request, response);

        if (user == null)
            return;

        String projectName = request.getParameter("name");

        if (projectName == null || projectName.trim().isEmpty()) {
            request.setAttribute("error", "Project name is required");
            request.getRequestDispatcher("/addProject.jsp").forward(request, response);

            return;
        }

        Project project = new Project(projectName, user.getId());
        projectDAO.createProject(project);

        response.sendRedirect(request.getContextPath() + "/projects");

    }

}
