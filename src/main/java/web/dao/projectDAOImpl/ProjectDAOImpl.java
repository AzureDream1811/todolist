package web.dao.projectDAOImpl;

import web.dao.ProjectDAO;
import web.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ProjectDAOImpl implements ProjectDAO {

  private final DataSource ds;

  public ProjectDAOImpl(DataSource ds) {
    this.ds = ds;
  }

  /**
   * Creates a new project in the database.
   *
   * @param project the project to create
   * @return true if the project was created successfully, false otherwise
   * @throws RuntimeException if an SQL exception occurs
   */
  public boolean createProject(Project project) {
    String sql = "INSERT INTO projects (name, user_id) VALUES (?, ?)";
    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

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

  /**
   * Retrieves a list of projects by the given user ID.
   *
   * @param userId the user ID to search for
   * @return a list of projects associated with the given user ID
   * @throws RuntimeException if an SQL exception occurs
   */
  public List<Project> getProjectsByUserId(int userId) {
    List<Project> projects = new ArrayList<>();
    String sql = "SELECT * FROM projects WHERE user_id = ?";
    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);) {

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
   * Retrieves a project by the given project ID and user ID.
   * 
   * @param projectId the project ID to search for
   * @param userId    the user ID to search for
   * @return the project object if found, null otherwise
   * @throws RuntimeException if an SQL exception occurs
   */
  public Project getProjectByIdAndUserId(int projectId, int userId) {
    String sql = "SELECT * FROM projects WHERE id = ? AND user_id = ?";
    try (Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);) {

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
  public Project mapResultSetToProject(ResultSet rs) throws SQLException {
    Project project = new Project();
    project.setId(rs.getInt("id"));
    project.setName(rs.getString("name"));
    project.setUserId(rs.getInt("user_id"));
    project.setCreatedAt(rs.getDate("created_at").toLocalDate());
    return project;
  }

  /**
   * Retrieves a list of all projects in the database.
   *
   * @return a list of all projects in the database
   * @throws RuntimeException if an SQL exception occurs
   */
  @Override
  public List<Project> getAllProjects() {
    List<Project> projects = new ArrayList<>();
    String sql = "SELECT * FROM projects";
    try (Connection connection = ds.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        Project project = mapResultSetToProject(resultSet);
        projects.add(project);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return projects;
  }

  @Override
  public void deleteProjectById(int id) {
    String sql = "DELETE FROM projects WHERE projects.id = ?";
    try (
        Connection connection = ds.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);) {
      statement.setInt(1, id);
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Searches for projects by name matching the query.
   *
   * @param userId the user ID to filter projects
   * @param query the search query string
   * @return a list of projects matching the search criteria
   */
  @Override
  public List<Project> searchProjects(int userId, String query) {
    List<Project> projects = new ArrayList<>();
    String sql = "SELECT * FROM projects WHERE user_id = ? AND name LIKE ? ORDER BY created_at DESC";

    try (Connection connection = ds.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, userId);
      String searchPattern = "%" + query + "%";
      statement.setString(2, searchPattern);
      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        Project project = mapResultSetToProject(rs);
        projects.add(project);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return projects;
  }
}