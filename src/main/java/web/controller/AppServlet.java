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
  private static final String ADD_TASK_PAGE = "/WEB-INF/views/component/AddTask.jsp";
  private static final String COMPLETED_PAGE = "/WEB-INF/views/app/Completed.jsp";
  private static final String PROJECTS_PAGE = "/WEB-INF/views/app/Projects.jsp";
  private static final String PROJECT_DETAIL_PAGE = "/WEB-INF/views/app/ProjectDetail.jsp";
  private static final String DELETE_TASK_PAGE = "/WEB-INF/views/component/DeleteTask.jsp";
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

    if (currentUser.isAdmin()) {
      response.sendRedirect(request.getContextPath() + "/admin");
      return;
    }

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
      case "delete-task":
        deleteTaskGet(request, response, currentUser);
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
      // THÊM DÒNG NÀY ĐẦU TIÊN: Thiết lập đọc dữ liệu từ Form là UTF-8
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");

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
      case "delete-task": // THÊM DÒNG NÀY
        deleteTaskPost(request, response, currentUser);
        break;
      default:
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        break;
    }
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
   * Handles the GET request for the add task page.
   * <p>
   * This method fetches the projects of the current user and passes them to the
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
  private void addTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      List<Project> projects = projectDAO.getProjectsByUserId(currentUser.getId());
      request.setAttribute("projects", projects);

      request.getRequestDispatcher(ADD_TASK_PAGE).forward(request, response);
    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error loading add task", "/app/tasks");
    }
  }

  /**
   * Handles the POST request for the add task page.
   * <p>
   * This method validates the input parameters and creates a new task.
   * If the task is created successfully, it redirects the user to the inbox page.
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
  private void addTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser)
          throws ServletException, IOException {
      try {
          request.setCharacterEncoding("UTF-8");

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

          // --- FIX 1: XỬ LÝ NGÀY THÁNG (BẮT BUỘC NOT NULL) ---
          LocalDate finalDueDate;
          if (dueDateStr != null && !dueDateStr.isEmpty()) {
              finalDueDate = LocalDate.parse(dueDateStr);
          } else {
              // Mặc định là hôm nay cho mọi trường hợp để an toàn với DB
              finalDueDate = LocalDate.now();
          }
          task.setDueDate(finalDueDate);

          // --- FIX 2: XỬ LÝ PRIORITY (QUAN TRỌNG - KHÔNG ĐỂ QUÁ 3) ---
          int priority;
          try {
              // Dựa trên bảng của bạn chỉ có 1, 2, 3. Mặc định gán là 3 (thấp nhất trong dải 1-3)
              priority = (priorityParam != null && !priorityParam.isEmpty()) ? Integer.parseInt(priorityParam) : 3;
              if (priority < 1 || priority > 3) {
                  priority = 3;
              }
          } catch (NumberFormatException e) {
              priority = 3;
          }
          task.setPriority(priority);

          // --- FIX 3: XỬ LÝ PROJECT ID ---
          int projectId = (projectIdParam != null && !projectIdParam.isEmpty()) ? Integer.parseInt(projectIdParam) : 0;
          task.setProjectIdObject(projectId > 0 ? projectId : null);

          task.setUserId(currentUser.getId());

          // Thực thi lưu xuống Database
          taskDAO.createTask(task);

          // Gửi email thông báo
          if (currentUser.getEmail() != null) {
              String mailContent = "<h3>New Task Added!</h3>" +
                      "<p>Title: <b>" + title + "</b></p>" +
                      "<p>Priority: <b>" + priority + "</b></p>" +
                      "<p>Due date: <b>" + finalDueDate + "</b></p>";
              web.utils.EmailUtils.sendEmailAsync(currentUser.getEmail(), "Task Notification", mailContent);
          }

          String redirectPath = determineRedirectPath(taskType);
          response.sendRedirect(request.getContextPath() + redirectPath);
      } catch (Exception e) {
          e.printStackTrace();
          WebUtils.sendError(request, response, "Error: " + e.getMessage(), "/app/tasks");
      }
  }

  /**
   * Determines the redirect path based on the task type.
   * If the task type is null or empty, it returns the default path /app/inbox.
   * Otherwise, it returns the path corresponding to the task type.
   * For example, if the task type is "today", it returns "/app/today".
   * If the task type is not recognized, it returns the default path /app/inbox.
   *
   * @param taskType the task type to determine the redirect path
   * @return the redirect path based on the task type
   */
  private String determineRedirectPath(String taskType) {
    if (taskType == null || taskType.isEmpty()) {
      return "/app/inbox";// default
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
  private void deleteTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      String taskIDParam = request.getParameter("taskID");

      if (taskIDParam == null || taskIDParam.isEmpty()) {
        WebUtils.sendError(request, response, "Task ID is required", "/app/inbox");
        return;
      }

      int taskID = Integer.parseInt(taskIDParam);

      List<Task> tasks = taskDAO.getTaskByIdAndUserId(taskID, currentUser.getId());

      if (tasks.isEmpty()) {
        WebUtils.sendError(request, response, "Task  not found", "/app/inbox");
        return;
      }

      Task task = tasks.get(0);

      request.setAttribute("task", task);

      String redirectPath = request.getParameter("redirect");
      request.setAttribute("redirectPath", redirectPath != null ? redirectPath : "/app/inbox");

      request.getRequestDispatcher(DELETE_TASK_PAGE).forward(request, response);
    } catch (NumberFormatException e) {
      WebUtils.sendError(request, response, "Invalid task ID format", "/app/inbox");
    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error loading delete tasks page", "/app/inbox");
    }
  }

  /**
   * Handles the POST request for the delete task page.
   * <p>
   * This method takes the task ID and confirmation parameters from the request.
   * If the task ID is null or empty, it sends an error response to the client.
   * If the confirmation parameter is not "true", it sends an error response to
   * the client.
   * If the task ID is found, it deletes the task and redirects the user to the
   * appropriate page based on the redirect path.
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
  public void deleteTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser)
      throws ServletException, IOException {
    try {
      String taskIDParam = request.getParameter("taskID");
      String confirm = request.getParameter("confirm");

      // Get taskType to reDirect to the correct page
      String redirectPath = request.getParameter("redirect");

      if (taskIDParam == null || taskIDParam.isEmpty()) {
        WebUtils.sendError(request, response, "Task ID is required", "/app/inbox");
        return;
      }

      // Check user confirmed deletion
      if (!"true".equals(confirm)) {
        WebUtils.sendError(request, response, "Invalid confirmation", "/app/inbox");
        return;
      }

      int taskID = Integer.parseInt(taskIDParam);

      List<Task> tasks = taskDAO.getTaskByIdAndUserId(taskID, currentUser.getId());
      if (tasks.isEmpty()) {
        WebUtils.sendError(request, response, "Task not found", "/app/inbox");
        return;
      }

      Task task = tasks.get(0);

      boolean success = taskDAO.deleteTask(task);

      if (success) {
        String finalRedirectPath = determineRedirectPathAfterDelete(redirectPath);
        response.sendRedirect(request.getContextPath() + finalRedirectPath);
        return;
      } else {
        WebUtils.sendError(request, response, "Error deleting task", determineRedirectPathAfterDelete(redirectPath));
      }
    } catch (Exception e) {
      WebUtils.sendError(request, response, "Error delete task", "/app/inbox");
    }
  }

  /**
   * Determines the redirect path after deleting a task.
   * If the current page path is null or empty, it returns the inbox page.
   * If the current page path contains "/app/today", it returns the today page.
   * If the current page path contains "/app/upcoming", it returns the upcoming
   * page.
   * Otherwise, it returns the inbox page.
   *
   * @param currentPagePath the current page path
   * @return the redirect path after deleting a task
   */
  private String determineRedirectPathAfterDelete(String currentPagePath) {
    if (currentPagePath == null || currentPagePath.isEmpty()) {
      return "/app/inbox";
    }

    if (currentPagePath.contains("/app/today")) {
      return "/app/today";
    } else if (currentPagePath.contains("/app/upcoming")) {
      return "/app/upcoming";
    } else {
      return "/app/inbox";
    }
  }

  /**
   * Handles the search request for tasks and projects.
   * Supports both AJAX requests (returns JSON) and regular requests (returns HTML page).
   *
   * @param request     the HttpServletRequest object
   * @param response    the HttpServletResponse object
   * @param currentUser the current logged-in user
   * @throws ServletException if an exception occurs during the servlet processing
   * @throws IOException      if an exception occurs during the input/output operations
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