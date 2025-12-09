package web.dao.taskDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import web.dao.TaskDAO;
import web.model.Task;
import web.utils.DatabaseUtils;

public class TaskDAOImpl implements TaskDAO {


  public void createTask(Task task) { //
    String sql = "INSERT INTO tasks (title, description, completed, priority, due_date, user_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try {
      Connection connection = DatabaseUtils.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, task.getTitle());
      statement.setString(2, task.getDescription());
      statement.setBoolean(3, task.isCompleted());
      statement.setInt(4, task.getPriority());
      statement.setDate(5, task.getDueDate() != null ? Date.valueOf(task.getDueDate()) : null);
      statement.setInt(6, task.getUserId());
      if (task.getProjectId() > 0) {
        statement.setInt(7, task.getProjectId());
      } else {
        statement.setNull(7, Types.INTEGER);
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

  public Task getTaskById(String taskId) {
    String sql = "SELECT * FROM tasks WHERE id = ?";
    try {
      Connection connection = DatabaseUtils.getConnection();
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
      Connection connection = DatabaseUtils.getConnection();
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

  private Task mapResultSetToTask(ResultSet rs) throws SQLException {
    Task task = new Task();
    task.setId(rs.getInt("id"));
    task.setTitle(rs.getString("title"));
    task.setDescription(rs.getString("description"));
    task.setCompleted(rs.getBoolean("completed"));
    task.setPriority(rs.getInt("priority"));
    Date due = rs.getDate("due_date");
    if (due != null) {
      task.setDueDate(due.toLocalDate());
    } else {
      task.setDueDate(null);
    }

    task.setUserId(rs.getInt("user_id"));
    task.setProjectId(rs.getInt("project_id"));

    return task;
  }

  public List<Task> getTodayTaskByUserID(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date = CURDATE() AND completed = false";

    try (Connection connection = DatabaseUtils.getConnection();
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

  public List<Task> getOverdueTaskByUserID(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date < CURDATE() AND completed = false";

    try (Connection connection = DatabaseUtils.getConnection();
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

  public List<Task> getUpcomingTasksByUserId(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date > CURDATE() AND completed = false";

    try {
      Connection connection = DatabaseUtils.getConnection();
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
      Connection connection = DatabaseUtils.getConnection();
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

  public List<Task> getCompletedTaskByUserId(int userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE completed = true AND user_id = ?";

    try (Connection connection = DatabaseUtils.getConnection();
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

  public List<Task> getTaskByIDandUserId(int taskID, int userID) {
      List<Task> tasks = new ArrayList<>();
      String sql = "SELECT * FROM tasks WHERE id = ? AND user_id = ?";

      try (Connection connection = DatabaseUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

          statement.setInt(1, taskID);
          statement.setInt(2, userID);

          ResultSet rs = statement.executeQuery();

          while (rs.next()){
              Task task = mapResultSetToTask(rs);
              tasks.add(task);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return tasks;
  }

  public boolean deleteTask (Task task) {
      String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

      try (Connection connection = DatabaseUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

          statement.setInt(1, task.getId());
          statement.setInt(2, task.getUserId());

          int affectRow = statement.executeUpdate();
          return affectRow > 0;

      } catch (Exception e) {
          e.printStackTrace();
      }
      return false;
  }

}