package web.dao;

import web.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ProjectDAO {
    boolean createProject(Project project);

    List<Project> getProjectsByUserId(int userId);

    Project getProjectByIdAndUserId(int projectId, int userId);

    List<Project> getAllProjects();

    void deleteProjectById(int id);

    Project mapResultSetToProject(ResultSet rs) throws SQLException;

    List<Project> searchProjects(int userId, String query);
}