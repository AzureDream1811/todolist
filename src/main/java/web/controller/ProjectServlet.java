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
            case "/delete":
                handleDeleteProject(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private void handleDeleteProject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = WebUtils.validateAndGetUser(request, response);

        if (user == null)
            return;

        String projectIdStr = request.getParameter("id");

        if (projectIdStr == null || projectIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/projects");
            return;
        }

        try{
            int projectId = Integer.parseInt(projectIdStr);
            projectDAO.deleteProjectById(projectId);

        } catch(NumberFormatException e){
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/projects");
    }

    /**
     * Handles the add project request.
     * <p>
     * This method validates the user and extracts the project name from the
     * request.
     * If the project name is empty, it sets the error attribute to "Project name is
     * required"
     * and forwards the request to the add project page. If the user is valid and
     * the
     * project name is not empty, it creates a new project with the given name and
     * the user's ID and adds it to the database. Finally, it redirects the user to
     * the projects page.
     * 
     * @param request  the HttpServletRequest object containing the request
     *                 parameters
     * @param response the HttpServletResponse object to send the response back to
     *                 the client
     * @throws IOException      if an exception occurs during the input/output
     *                          operations
     * @throws ServletException if an exception occurs during the servlet processing
     */
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