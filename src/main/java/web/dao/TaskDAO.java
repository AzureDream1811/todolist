package web.dao;

import web.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TaskDAO {
    void createTask(Task task);
    Task getTaskById(String taskId);
    List<Task> getTasksByUserId(int userId);
    Task mapResultSetToTask(ResultSet rs) throws SQLException;
    List<Task> getTodayTaskByUserID(int userId);
    List<Task> getOverdueTaskByUserID(int userId);
    List<Task> getUpcomingTasksByUserId(int userId);
    List<Task> getTasksByProjectIdAndUserId(int projectId, int userId);
    List<Task> getCompletedTaskByUserId(int userId);
    List<Task> getTaskByIDandUserId(int taskID, int userID);
    boolean deleteTask (Task task);
}