package web.dao;

import web.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TaskDAO {
    void createTask(Task task);

    List<Task> getAllTasks();

    List<Task> getTasksByUserId(int userId);

    List<Task> getTodayTaskByUserID(int userId);

    List<Task> getOverdueTaskByUserID(int userId);

    List<Task> getUpcomingTasksByUserId(int userId);

    List<Task> getTasksByProjectIdAndUserId(int projectId, int userId);

    List<Task> getCompletedTaskByUserId(int userId);

    List<Task> getTaskByIdAndUserId(int taskID, int userID);

    boolean deleteTask(Task task);

    void deleteTaskById(int id);

    Task mapResultSetToTask(ResultSet rs) throws SQLException;
}