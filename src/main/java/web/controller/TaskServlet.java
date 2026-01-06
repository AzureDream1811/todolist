package web.controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.dao.DAOFactory;
import web.dao.TaskDAO;
import web.model.Task;
import web.model.User;
import web.utils.EmailUtils;
import web.utils.WebUtils;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {
    DAOFactory factory = DAOFactory.getInstance();
    TaskDAO taskDAO = factory.getTaskDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if ("/detail".equals(path)) {
            showTaskDetail(request, response);
        }

    }

    /**
     * Shows the task detail page based on the given task ID.
     * <p>
     * This method takes the task ID parameter from the request and retrieves the task
     * with the given ID from the database. If the task ID is null or empty, it
     * sends a bad request response to the client. If the task is not found, it
     * sets the error attribute to "task not found" and forwards the request to
     * the error page. If the task is found, it sets the task attribute to the
     * found task and forwards the request to the task detail page.
     * 
     * @param request     the HttpServletRequest object containing the request
     *                    parameters
     * @param response    the HttpServletResponse object to send the response back
     *                    to the client
     * @throws IOException      if an exception occurs during the input/output
     *                          operations
     * @throws ServletException if an exception occurs during the servlet processing
     */
    private void showTaskDetail(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String idStr = request.getParameter("taskId");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(idStr);
        Task task = taskDAO.getTaskById(id);
        if (task == null) {
            request.setAttribute("error", "task not found");
            return;
        }
        request.setAttribute("task", task);
        request.getRequestDispatcher("/WEB-INF/views/component/taskDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if ("/update".equals(path)) {
            updateTask(request, response);
        }
    }

    /**
     * Updates a task in the database based on the given parameters.
     * <p>
     * This method takes the task ID, description, priority, due date, completed at
     * and project ID parameters from the request and updates the task with the
     * given ID in the database.
     * </p>
     * <p>
     * The process includes:
     * 1. Validating and retrieving the current user from the session via WebUtils.
     * 2. Parsing task details such as description, priority, due date, completion date, and project ID.
     * 3. Updating the task in the database through taskDAO.
     * 4. Sending an asynchronous confirmation email to the user if they have a registered email address.
     * 5. Redirecting the user to the task detail page upon success.
     * </p>
     * * If the task ID is null or empty, it sends a bad request response to the client.
     * If the task is not found, it sets the error attribute to "task not found"
     * and forwards the request to the error page.
     * * @param request     the HttpServletRequest object containing the request
     * parameters
     * @param response    the HttpServletResponse object to send the response back
     * to the client
     * @throws IOException      if an exception occurs during the input/output
     * operations
     * @throws ServletException if an exception occurs during the servlet processing
     */
    private void updateTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User currentUser = WebUtils.validateAndGetUser(request, response);
        if (currentUser == null) return;

        Task task = new Task();
        task.setId(Integer.parseInt(request.getParameter("id")));
        task.setDescription(request.getParameter("description"));
        task.setPriority(Integer.parseInt(request.getParameter("priority")));
        String dueDateStr = request.getParameter("dueDate");
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            task.setDueDate(LocalDate.parse(dueDateStr));
        }
        String completedAtStr = request.getParameter("completedAt");
        if (completedAtStr != null && !completedAtStr.isEmpty()) {
            task.setCompletedAt(LocalDate.parse(completedAtStr));
        }
        String projectId = request.getParameter("projectId");
        if (projectId != null && !projectId.isEmpty()) {
            task.setProjectId(Integer.parseInt(projectId));
        }
        taskDAO.updateTask(task);
        if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
            EmailUtils.sendEmailAsync(
                    currentUser.getEmail(),
                    "Cập nhật công việc thành công",
                    "<h3>Thông báo TodoList</h3>" +
                            "<p>Bạn vừa cập nhật Task: <b>" + task.getDescription() + "</b></p>"
            );
        }
        response.sendRedirect(request.getContextPath() + "/tasks/detail?taskId=" + task.getId());
    }
}
