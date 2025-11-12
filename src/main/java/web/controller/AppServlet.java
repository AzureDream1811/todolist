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

@WebServlet({ "/app/*" })
public class AppServlet extends HttpServlet {
  private static final String INBOX_PAGE = "/WEB-INF/views/app/Inbox.jsp";
  private static final String TODAY_PAGE = "/WEB-INF/views/app/Today.jsp";
  private static final String UPCOMING_PAGE = "/WEB-INF/views/app/Upcoming.jsp";
  private static final String ADD_TASK_PAGE = "/WEB-INF/views/component/AddTask.jsp";
  private static final String COMPLETED_PAGE = "/WEB-INF/views/app/Completed.jsp";
  private static final String PROJECTS_PAGE = "/WEB-INF/views/app/Projects.jsp";
  private static final String PROJECT_DETAIL_PAGE = "/WEB-INF/views/app/ProjectDetail.jsp";

  private final TaskDAO taskDAO = new TaskDAO();
  private final ProjectDAO projectDAO = new ProjectDAO();

  /**
   * Handles the GET request for the app page.
   * <p>
   * This method routes the request to the appropriate handler based on the path.
   * If the path is null or empty, it redirects the request to the inbox page.
   * If the path is not recognized, it redirects the request to the inbox page.
   *
   * @param request  the HttpServletRequest object containing the request
   *                 parameters
   * @param response the HttpServletResponse object to send the response back to
   *                 the client
   * @throws ServletException if an exception occurs during the servlet processing
   * @throws IOException      if an exception occurs during the input/output
   *                          operations
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    User currentUser = WebUtils.validateAndGetUser(request, response);
    if (currentUser == null)
      return;

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
        showProjects(request, response, currentUser);
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
    if (currentUser == null)
      return;

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

  private void showInbox(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
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

  private void addTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
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

  private void addTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      String title = request.getParameter("title");
      String description = request.getParameter("description");
      boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
      String dueDate = request.getParameter("dueDate");
      String priorityParam = request.getParameter("priority");
      int priority = (priorityParam != null && !priorityParam.isEmpty()) ? Integer.parseInt(priorityParam) : 1;
      String projectIdParam = request.getParameter("projectId");
      int projectId = (projectIdParam != null && !projectIdParam.isEmpty()) ? Integer.parseInt(projectIdParam) : 0;

//    Get taskType to reDirect to the correct page
      String taskType = request.getParameter("taskType");

      Task task = new Task();
      task.setTitle(title);
      task.setDescription(description.isEmpty() ? null : description);
      task.setCompleted(completed);
      task.setDueDate(dueDate.isEmpty() ? null : LocalDate.parse(dueDate));
      task.setPriority(Math.max(priority, 1));
      task.setProjectId(Math.max(projectId, 0));
      task.setUserId(currentUser.getId());

      taskDAO.createTask(task);

      String redirectPath = determineRedirectPath(taskType);
      response.sendRedirect(request.getContextPath() + redirectPath);

      response.sendRedirect(request.getContextPath() + "/app/inbox");
    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error adding task", "/app/tasks");
    }

  }

  private String determineRedirectPath(String taskType) {
      if(taskType == null || taskType.isEmpty()){
          return "/app/inbox";//default
      }

      switch (taskType){
          case "today":
              return "/app/today";
          case "upcoming":
              return "/app/upcoming";
          case "inbox":
          default:
              return "/app/inbox";
      }
  }

  private void showToday(HttpServletRequest request, HttpServletResponse response, User currentUser)
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

  private void showUpcoming(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      List<Task> upcomingTasks = taskDAO.getUpcomingTasksByUserId(currentUser.getId());

      request.setAttribute("upcomingTasks", upcomingTasks);

      request.getRequestDispatcher(UPCOMING_PAGE).forward(request, response);
    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error loading upcoming tasks", "/app/upcoming");
    }
  }

  private void showCompletedTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      List<Task> completedTasks = taskDAO.getCompletedTaskByUserId(currentUser.getId());

      request.setAttribute("CompletedTasks", completedTasks);

      request.getRequestDispatcher(COMPLETED_PAGE).forward(request, response);

    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error loading completed tasks", "/app/inbox");
    }
  }

  private void showProjects(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      String idParam = request.getParameter("id");
      if (idParam != null) {
        showProjectDetails(request, response, currentUser, Integer.parseInt(idParam));
        return;
      }

      List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
      request.setAttribute("projects", projects);
      request.getRequestDispatcher(PROJECTS_PAGE).forward(request, response);

    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error loading projects", "/app/inbox");
    }
  }

  private void showProjectDetails(HttpServletRequest request, HttpServletResponse response, User currentUser,
      int projectId) {
    try {
      Project project = projectDAO.getProjectByIdAndUserId(projectId, currentUser.getId());
      if (project == null) {
        WebUtils.sendError(request, response, "Project not found", "/app/projects");
        return;
      }

      // get tasks for the project and current user
      List<Task> projectTasks = taskDAO.getTasksByProjectIdAndUserId(projectId, currentUser.getId());

      // set attributes and forward to project detail page
      request.setAttribute("currentProject", project);
      request.setAttribute("projectTasks", projectTasks);
      request.getRequestDispatcher(PROJECT_DETAIL_PAGE).forward(request, response);

    } catch (Exception e) {
      e.printStackTrace();

    }
  }
}
