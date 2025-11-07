package web.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import web.beans.Task;
import web.utils.DatabaseUtils;

public class TaskDAO {


    /**
     * Creates a new task in the database.
     *
     * @param task the task to create
     * @return true if the task was created successfully, false otherwise
     */
    public boolean createTask(Task task) { //
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
            if (affectedRows == 0) return false;

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    task.setId(resultSet.getInt(1));
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
}
