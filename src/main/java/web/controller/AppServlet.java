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
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/app/*"})
public class AppServlet extends HttpServlet {
    private static final String INBOX_PAGE = "/WEB-INF/views/app/Inbox.jsp";
    private static final String TODAY_PAGE = "/WEB-INF/views/app/Today.jsp";
    private static final String UPCOMING_PAGE = "/WEB-INF/views/app/Upcoming.jsp";
    private static final String ADD_TASK_PAGE = "/WEB-INF/views/app/AddTask.jsp";
    private static final String COMPLETED_PAGE = "/WEB-INF/views/app/Completed.jsp";
    private static final String PROJECTS_PAGE = "/WEB-INF/views/app/Projects.jsp";
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

        String action = WebUtils.getActionFormPath(request, response);
        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Route to appropriate handler based on action
        switch (action) {
            case "today":
                showToday(request, response, currentUser);
                break;
            case "tasks":
                addTaskGet(request, response, currentUser);
                break;
            case "projects":
                request.getRequestDispatcher(PROJECTS_PAGE).forward(request, response);
                break;
            case "upcoming":
                showUpcoming(request, response, currentUser);
                break;
            case "completed":
                showCompletedTask(request, response, currentUser);
                break;
            case "inbox":
            default:
                showInbox(request, response, currentUser);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        String action = WebUtils.getActionFormPath(request, response);
        if (action == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Route to appropriate handler based on action
        switch (action) {
            case "tasks":
                addTaskPost(request, response, currentUser);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            request.getRequestDispatcher(INBOX_PAGE).forward(request, response);
        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading inbox", "/app/inbox");
        }
    }

    private void addTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        try {
            // pass projects to jsp
            List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
            request.setAttribute("projects", projects);

            // forward to add task page
            request.getRequestDispatcher(ADD_TASK_PAGE).forward(request, response);
        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading add task", "/app/tasks");
        }
    }

    private void addTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws  ServletException, IOException{
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
            String dueDate = request.getParameter("dueDate");
            int priority = Integer.parseInt(request.getParameter("priority"));
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description.isEmpty() ? null : description);
            task.setCompleted(completed);
            task.setDueDate(dueDate.isEmpty() ? null : LocalDate.parse(dueDate));
            task.setPriority(Math.max(priority, 1));
            task.setProjectId(Math.max(projectId, 0));
            task.setUserId(currentUser.getId());

            taskDAO.createTask(task);

            response.sendRedirect(request.getContextPath() + "/app/inbox");
        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error adding task", "/app/tasks");
        }

    }

    public void showToday(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        try {
            List<Task> overdueTasks = taskDAO.getOverdueTaskByUserID(currentUser.getId());
            List<Task> todayTasks = taskDAO.getTodayTaskByUserID(currentUser.getId());

            request.setAttribute("overdueTasks", overdueTasks);
            request.setAttribute("todayTasks", todayTasks);

            List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
            request.setAttribute("projects", projects);

            request.getRequestDispatcher(TODAY_PAGE).forward(request, response);

        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading today's tasks", "/app/inbox");
        }
    }

    public void showUpcoming(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException {
        try {
            List<Task> upcomingTasks = taskDAO.getUpcomingTasksByUserId(currentUser.getId());

            request.setAttribute("upcomingTasks", upcomingTasks);

            request.getRequestDispatcher(UPCOMING_PAGE).forward(request, response);
        } catch (Exception e){
            WebUtils.sendError(request, response, "Error loading upcoming tasks", "/app/upcoming");
        }
    }

    public void showCompletedTask(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException{
        try {
            List<Task> completedTasks = taskDAO.getCompletedTaskByUserId(currentUser.getId());

            request.setAttribute("CompletedTasks", completedTasks);

            request.getRequestDispatcher(COMPLETED_PAGE).forward(request, response);

        } catch (Exception e) {
            WebUtils.sendError(request, response, "Error loading completed tasks", "/app/inbox");
        }
    }
}
