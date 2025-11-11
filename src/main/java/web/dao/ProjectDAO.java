package web.dao;

import web.beans.Project;
import web.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

  /**
   * Creates a new project in the database.
   *
   * @param project the project to create
   * @return true if the project was created successfully, false otherwise
   * @throws RuntimeException if an SQL exception occurs
   */
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

  /**
   * Retrieves a project by its ID and user ID.
   *
   * @param projectId the project ID to search for
   * @param userId    the user ID to search for
   * @return the project object if found, null otherwise
   * @throws RuntimeException if an SQL exception occurs
   */

  /************* ✨ Windsurf Command ⭐ *************/
  /**
   * Retrieves a project by its ID and user ID.
   * 
   * @param projectId the project ID to search for
   * @param userId    the user ID to search for
   * @return the project object if found, null otherwise
   * @throws RuntimeException if an SQL exception occurs
   */
  /******* 83cba269-049c-4687-ae88-e6b617e49fba *******/
  public Project getProjectByIdAndUserId(int projectId, int userId) {
    String sql = "SELECT * FROM projects WHERE id = ? AND user_id = ?";
    try {
      Connection connection = DatabaseUtils.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, projectId);
      statement.setInt(2, userId);
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        return mapResultSetToProject(rs);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }


  /**
   * Maps a given ResultSet to a Project object.
   * 
   * @param rs the ResultSet containing the project data
   * @return the mapped Project object
   * @throws SQLException if an SQL exception occurs
   */
  private Project mapResultSetToProject(ResultSet rs) throws SQLException {
    Project project = new Project();
    project.setId(rs.getInt("id"));
    project.setName(rs.getString("name"));
    project.setUserId(rs.getInt("user_id"));
    project.setCreatedAt(rs.getDate("created_at").toLocalDate());
    return project;
  }
}
