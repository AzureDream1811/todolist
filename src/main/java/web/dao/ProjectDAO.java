  package web.dao;

  import web.beans.Project;
  import web.beans.Task;
  import web.utils.DatabaseUtils;

  import java.sql.*;
  import java.util.ArrayList;
  import java.util.List;

  public class ProjectDAO {

      public boolean createProject(Project project) {
          String sql = "INSERT INTO projects (name, user_id) VALUES (?, ?)";
          try {
              Connection connection = DatabaseUtils.getConnection();
              PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
              statement.setString(1, project.getName());
              statement.setInt(2, project.getUserId());

              int affectedRows = statement.executeUpdate();

              if (affectedRows == 0) {
                  return false;
              }

              try (ResultSet resultSet = statement.getGeneratedKeys()) {
                  if (resultSet.next()) {
                      project.setId(resultSet.getInt(1));
                      return true;
                  }
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
          return false;
      }

      public List<Project> getProjectsByUserId(int userId) {
          List<Project> projects = new ArrayList<>();
          String sql = "SELECT * FROM projects WHERE user_id = ?";
          try {
              Connection connection = DatabaseUtils.getConnection();
              PreparedStatement statement = connection.prepareStatement(sql);
              statement.setInt(1, userId);
              ResultSet rs = statement.executeQuery();

              while (rs.next()) {
                  Project project = mapResultSetToProject(rs);
                  projects.add(project);
              }

          } catch (Exception e) {
              e.printStackTrace();
          }

          return projects;
      }

      public List<Task> getTasksByProjectId(int projectId){
          List<Task> tasks = new ArrayList<>();
          String sql = "SELECT * FROM tasks WHERE project_id = ?";
          try {
              Connection connection = DatabaseUtils.getConnection();
              PreparedStatement statement = connection.prepareStatement(sql);
              statement.setInt(1, projectId);
              ResultSet rs = statement.executeQuery();

              while (rs.next()) {
                  Task task = new Task();
                  task.setId(rs.getInt("id"));
                  task.setTitle(rs.getString("title"));
                  task.setDescription(rs.getString("description"));
                  task.setCompleted(rs.getBoolean("completed"));
                  Date dueDate = rs.getDate("due_date");
                  if (dueDate != null) {
                      task.setDueDate(dueDate.toLocalDate());
                  }
                  task.setPriority(rs.getInt("priority"));
                  task.setProjectId(rs.getInt("project_id"));
                  task.setUserId(rs.getInt("user_id"));
                  task.setCreatedAt(rs.getDate("created_at").toLocalDate());

                  tasks.add(task);
              }

          } catch (Exception e) {
              e.printStackTrace();
          }

          return tasks;
      }

      private Project mapResultSetToProject(ResultSet rs) throws SQLException {
          Project project = new Project();
          project.setId(rs.getInt("id"));
          project.setName(rs.getString("name"));
          project.setUserId(rs.getInt("user_id"));
          project.setCreatedAt(rs.getDate("created_at").toLocalDate());
          return project;
      }
  }
