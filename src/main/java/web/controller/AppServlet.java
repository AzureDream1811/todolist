package web.controller;

import web.model.Project;
import web.model.Task;
import web.dao.DAOFactory;
import web.dao.ProjectDAO;
import web.dao.TaskDAO;
import web.model.User;
import web.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/app/*")
public class AppServlet extends HttpServlet {
  private static final String INBOX_PAGE = "/WEB-INF/views/app/Inbox.jsp";
  private static final String TODAY_PAGE = "/WEB-INF/views/app/Today.jsp";
  private static final String UPCOMING_PAGE = "/WEB-INF/views/app/Upcoming.jsp";
  private static final String COMPLETED_PAGE = "/WEB-INF/views/app/Completed.jsp";
  private static final String PROJECTS_PAGE = "/WEB-INF/views/app/Projects.jsp";
  private static final String PROJECT_DETAIL_PAGE = "/WEB-INF/views/app/ProjectDetail.jsp";
  private static final String SEARCH_RESULTS_PAGE = "/WEB-INF/views/app/SearchResults.jsp";

  private final DAOFactory factory = DAOFactory.getInstance();
  private final TaskDAO taskDAO = factory.getTaskDAO();
  private final ProjectDAO projectDAO = factory.getProjectDAO();

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

    // Admins can also use the app - no redirect needed

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
      case "projects":
        showProjects(request, response, currentUser);
        break;
      case "upcoming":
        showUpcoming(request, response, currentUser);
        break;
      case "completed":
        showCompletedTask(request, response, currentUser);
        break;
      case "search":
        handleSearch(request, response, currentUser);
        break;
      case "inbox":
      default:
        showInbox(request, response, currentUser);
        break;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");

      // AppServlet only handles views, POST requests for tasks go to TaskServlet
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  /**
   * Handles the GET request for the inbox page.
   * <p>
   * This method fetches the tasks and projects of the current user and passes
   * them to the jsp.
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

  /**
   * Handles the GET request for the today's tasks page.
   * <p>
   * This method fetches the overdue tasks and today's tasks of the current user
   * and passes them to the jsp.
   * It also fetches the projects of the current user and passes them to the jsp.
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
  private void showToday(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      List<Task> overdueTasks = taskDAO.getOverdueTasksByUserId(currentUser.getId());
      List<Task> todayTasks = taskDAO.getTodayTasksByUserId(currentUser.getId());

      request.setAttribute("overdueTasks", overdueTasks);
      request.setAttribute("todayTasks", todayTasks);

      List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
      request.setAttribute("projects", projects);

      request.getRequestDispatcher(TODAY_PAGE).forward(request, response);

    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error loading today's tasks", "/app/inbox");
    }
  }

  /**
   * Handles the GET request for the upcoming tasks page.
   * <p>
   * This method fetches the upcoming tasks of the current user and passes them to
   * the jsp.
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

  /**
   * Handles the GET request for the completed tasks page.
   * <p>
   * This method fetches the completed tasks of the current user and passes them
   * to the jsp.
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
  private void showCompletedTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
          throws ServletException, IOException {
      try {
          List<Task> completedTasks = taskDAO.getCompletedTaskByUserId(currentUser.getId());
          java.util.Map<java.time.LocalDate, List<Task>> groupedTasks = new java.util.LinkedHashMap<>();
          completedTasks.sort((t1, t2) -> t2.getCompletedAt().compareTo(t1.getCompletedAt()));

          for (Task task : completedTasks) {
              LocalDate date = task.getCompletedAt();
              groupedTasks.computeIfAbsent(date, k -> new java.util.ArrayList<>()).add(task);
          }

          request.setAttribute("groupedTasks", groupedTasks);
          request.setAttribute("totalCompleted", completedTasks.size());

          request.getRequestDispatcher(COMPLETED_PAGE).forward(request, response);

      } catch (Exception e) {
          WebUtils.sendError(request, response, "Error loading completed tasks", "/app/inbox");
      }
  }

  /**
   * Handles the GET request for the projects page.
   * <p>
   * This method fetches the projects of the current user and passes them to the
   * jsp.
   * If the id parameter is not null, it redirects the request to the project
   * details page.
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

  /**
   * Handles the GET request for the project details page.
   * <p>
   * This method fetches the project with the given ID and the current user.
   * If the project is not found, it sends an error response to the client.
   * If an exception occurs during the processing, it sends an error response to
   * the client.
   *
   * @param request     the HttpServletRequest object containing the request
   *                    parameters
   * @param response    the HttpServletResponse object to send the response back
   *                    to the client
   * @param currentUser the current user
   * @param projectId   the project ID to search for
   * @throws ServletException if an exception occurs during the servlet processing
   * @throws IOException      if an exception occurs during the input/output
   *                          operations
   */
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

  /**
   * Handles the GET request for the delete task page.
   * <p>
   * This method fetches the task with the given ID and the current user.
   * If the task is not found, it sends an error response to the client.
   * If an exception occurs during the processing, it sends an error response to
   * the client.
   *
   * @param request     the HttpServletRequest object containing the request
   *                    parameters
   * @param response    the HttpServletResponse object to send the response back
   *                    to the client
   * @param currentUser the current user
   * @throws ServletException if an exception occurs during the servlet processing
   * @throws IOException      if an exception occurs during the input/output
   *                          operations
   */
  private void handleSearch(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    String query = request.getParameter("q");
    String isAjax = request.getParameter("ajax");

    if (query == null || query.trim().isEmpty()) {
      if ("true".equals(isAjax)) {
        sendJsonResponse(response, "{\"tasks\":[],\"projects\":[]}");
      } else {
        response.sendRedirect(request.getContextPath() + "/app/inbox");
      }
      return;
    }

    query = query.trim();

    try {
      // Search tasks and projects
      List<Task> tasks = taskDAO.searchTasks(currentUser.getId(), query);
      List<Project> projects = projectDAO.searchProjects(currentUser.getId(), query);

      if ("true".equals(isAjax)) {
        // Return JSON for AJAX requests (quick search dropdown)
        StringBuilder json = new StringBuilder();
        json.append("{\"tasks\":[");
        for (int i = 0; i < tasks.size(); i++) {
          Task task = tasks.get(i);
          json.append("{\"id\":").append(task.getId())
              .append(",\"title\":\"").append(escapeJson(task.getTitle())).append("\"}");
          if (i < tasks.size() - 1) json.append(",");
        }
        json.append("],\"projects\":[");
        for (int i = 0; i < projects.size(); i++) {
          Project project = projects.get(i);
          json.append("{\"id\":").append(project.getId())
              .append(",\"name\":\"").append(escapeJson(project.getName())).append("\"}");
          if (i < projects.size() - 1) json.append(",");
        }
        json.append("]}");
        sendJsonResponse(response, json.toString());
      } else {
        // Return HTML page for regular requests
        request.setAttribute("tasks", tasks);
        request.setAttribute("projects", projects);
        request.setAttribute("searchQuery", query);
        request.setAttribute("totalResults", tasks.size() + projects.size());
        request.getRequestDispatcher(SEARCH_RESULTS_PAGE).forward(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if ("true".equals(isAjax)) {
        sendJsonResponse(response, "{\"error\":\"Search failed\",\"tasks\":[],\"projects\":[]}");
      } else {
        WebUtils.sendError(request, response, "Error performing search", "/app/inbox");
      }
    }
  }

  /**
   * Sends a JSON response to the client.
   *
   * @param response the HttpServletResponse object
   * @param json     the JSON string to send
   * @throws IOException if an exception occurs during writing the response
   */
  private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    out.print(json);
    out.flush();
  }

  /**
   * Escapes special characters in a string for JSON output.
   *
   * @param text the string to escape
   * @return the escaped string
   */
  private String escapeJson(String text) {
    if (text == null) return "";
    return text.replace("\\", "\\\\")
               .replace("\"", "\\\"")
               .replace("\n", "\\n")
               .replace("\r", "\\r")
               .replace("\t", "\\t");
  }
}