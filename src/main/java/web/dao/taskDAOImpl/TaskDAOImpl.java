package web.dao.taskDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import web.dao.TaskDAO;
import web.model.Task;

public class TaskDAOImpl implements TaskDAO {

  private final DataSource ds;

  public TaskDAOImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Creates a new task in the database.
   *
   * @param task the task to create
   */
  public void createTask(Task task) { //
    String sql = "INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try {
      Connection connection = ds.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, task.getTitle());
      statement.setString(2, task.getDescription());
      statement.setInt(3, task.getPriority());
      statement.setDate(4, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
      statement.setDate(5, task.getCompletedAt() != null ? Date.valueOf(task.getCompletedAt()) : null);
      statement.setInt(6, task.getUserId());
      Integer projectIdObj = task.getProjectIdObject();
      if (projectIdObj != null && projectIdObj > 0) {
        statement.setInt(7, projectIdObj);
      } else {
        statement.setNull(7, Types.BIGINT);
      }

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0)
        return;

      try (ResultSet resultSet = statement.getGeneratedKeys()) {
        if (resultSet.next()) {
          task.setId(resultSet.getInt(1));
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves a task by its ID.
   *
   * @param taskId the task ID to search for
   * @return a task object if found, null otherwise
   * @throws RuntimeException if an SQL exception occurs
   */

  public Task getTaskById(String taskId) {
    String sql = "SELECT * FROM tasks WHERE id = ?";
    try {
      Connection connection = ds.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1, taskId);

      try (ResultSet rs = statement.executeQuery()) {
        return rs.next() ? mapResultSetToTask(rs) : null;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves a list of tasks by the given user ID.
   *
   * @param userId the user ID to search for
   * @return a list of tasks associated with the given user ID
   */
  public List<Task> getTasksByUserId(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ?";

    try {
      Connection connection = ds.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);
        tasks.add(task);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return tasks;
  }

  /************* ✨ Windsurf Command ⭐ *************/
  /**
   * Maps a given ResultSet to a Task object.
   * 
   * @param rs the ResultSet containing the task data
   * @return the mapped Task object
   * @throws SQLException if an SQL exception occurs
   */
  /******* 1ee865e3-a864-4c90-b50c-7980f261459d *******/
  public Task mapResultSetToTask(ResultSet rs) throws SQLException {
    Task task = new Task();
    task.setId(rs.getInt("id"));
    task.setTitle(rs.getString("title"));
    task.setDescription(rs.getString("description"));
    task.setPriority(rs.getInt("priority"));
    Date due = rs.getDate("due_date");
    if (due != null) {
      task.setDueDate(due.toLocalDate());
    } else {
      task.setDueDate(null);
    }

    Date completed = rs.getDate("completed_at");
    if (completed != null) {
      task.setCompletedAt(completed.toLocalDate());
    } else {
      task.setCompletedAt(null);
    }

    task.setUserId(rs.getInt("user_id"));
    Object projObj = rs.getObject("project_id");
    if (projObj != null) {
      task.setProjectId(rs.getInt("project_id"));
    } else {
      task.setProjectIdObject(null);
    }

    return task;
  }

  /**
   * Retrieves a list of tasks by the given user ID which are due today.
   * 
   * @param userId the user ID to search for
   * @return a list of tasks associated with the given user ID which are due today
   */
  public List<Task> getTodayTaskByUserID(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date = CURDATE() AND completed_at IS NULL";

    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);
        tasks.add(task);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  /**
   * Retrieves a list of tasks by the given user ID which are overdue.
   * 
   * @param userId the user ID to search for
   * @return a list of tasks associated with the given user ID which are overdue
   */
  public List<Task> getOverdueTaskByUserID(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date < CURDATE() AND completed_at IS NULL";

    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);
        tasks.add(task);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  /**
   * Retrieves a list of tasks by the given user ID which are due in the future.
   * 
   * @param userId the user ID to search for
   * @return a list of tasks associated with the given user ID which are due in
   *         the future
   * @throws RuntimeException if an SQL exception occurs
   */
  public List<Task> getUpcomingTasksByUserId(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date > CURDATE() AND completed_at IS NULL";

    try {
      Connection connection = ds.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);
        tasks.add(task);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return tasks;
  }

  /**
   * Retrieves a list of tasks by the given project ID and user ID.
   *
   * @param projectId the project ID to search for
   * @param userId    the user ID to search for
   * @return a list of tasks associated with the given project ID and user ID
   * @throws RuntimeException if an SQL exception occurs
   */
  public List<Task> getTasksByProjectIdAndUserId(int projectId, int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE project_id = ? AND user_id = ?";
    try {
      Connection connection = ds.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, projectId);
      statement.setInt(2, userId);

      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);

        tasks.add(task);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return tasks;
  }

  /**
   * Retrieves a list of tasks by the given user ID which are completed.
   * 
   * @param userId the user ID to search for
   * @return a list of tasks associated with the given user ID which are completed
   * @throws RuntimeException if an SQL exception occurs
   */
  public List<Task> getCompletedTaskByUserId(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE completed_at IS NOT NULL AND user_id = ?";

    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);
        tasks.add(task);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tasks;
  }

  /**
   * Retrieves a list of tasks by the given task ID and user ID.
   * 
   * @param taskID the task ID to search for
   * @param userId the user ID to search for
   * @return a list of tasks associated with the given task ID and user ID
   * @throws RuntimeException if an SQL exception occurs
   */
  public List<Task> getTaskByIdAndUserId(int taskID, int userID) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE id = ? AND user_id = ?";

    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, taskID);
      statement.setInt(2, userID);

      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Task task = mapResultSetToTask(rs);
        tasks.add(task);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tasks;
  }

  /**
   * Deletes a task from the database given its ID and user ID.
   * 
   * @param task the task object to delete
   * @return true if the task was successfully deleted, false otherwise
   * @throws RuntimeException if an SQL exception occurs
   */
  public boolean deleteTask(Task task) {
    String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, task.getId());
      statement.setInt(2, task.getUserId());

      int affectRow = statement.executeUpdate();
      return affectRow > 0;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

/**
 * Retrieves a list of all tasks in the database.
 * 
 * @return a list of all tasks in the database
 * @throws RuntimeException if an SQL exception occurs
 */
  @Override
  public List<Task> getAllTasks() {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks";

    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Task task = mapResultSetToTask(resultSet);
        tasks.add(task);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return tasks;
  }

}