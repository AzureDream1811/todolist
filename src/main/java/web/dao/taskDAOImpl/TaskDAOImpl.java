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
     * Retrieves a list of tasks by the given user ID.
     *
     * @param userId the user ID to search for
     * @return a list of tasks associated with the given user ID
     */
    public List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        // SỬA SQL: Thêm điều kiện lọc task chưa hoàn thành
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND completed_at IS NULL";

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

    /*
     * Maps a given ResultSet to a Task object.
     *
     * @param rs the ResultSet containing the task data
     * 
     * @return the mapped Task object
     * 
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
        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDate());
        } else {
            task.setCreatedAt(null);
        }

        return task;
    }

    /**
     * Retrieves a list of tasks by the given user ID which are due today.
     *
     * @param userId the user ID to search for
     * @return a list of tasks associated with the given user ID which are due today
     */
    public List<Task> getTodayTasksByUserId(int userId) {
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
    public List<Task> getOverdueTasksByUserId(int userId) {
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
     * @return a list of tasks associatecompleteTaskd with the given user ID which are completed
     * @throws RuntimeException if an SQL exception occurs
     */
    public List<Task> getCompletedTaskByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        // JOIN để lấy thêm cột name của project
        String sql = "SELECT t.*, p.name as project_name " +
                "FROM tasks t " +
                "LEFT JOIN projects p ON t.project_id = p.id " +
                "WHERE t.completed_at IS NOT NULL AND t.user_id = ? " +
                "ORDER BY t.completed_at DESC";

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Task task = mapResultSetToTask(rs);
                // Lưu tên project vào một field tạm thời (bạn có thể thêm field 'projectName' vào class Task)
                // Hoặc đơn giản là kiểm tra project_id trong JSP
                tasks.add(task);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tasks;
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

    /**
     * Deletes a task from the database given its ID.
     * 
     * @param id the task ID to delete
     * @throws RuntimeException if an SQL exception occurs
     */
    @Override
    public void deleteTaskById(int id) {
        String sql = "DELETE FROM tasks WHERE tasks.id = ?";
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
     * Updates a task in the database.
     *
     * @param task the task to update
     * @throws RuntimeException if an SQL exception occurs
     */
    @Override
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET description = ?, priority = ?, due_date = ?, completed_at = ?, project_id = ? WHERE id = ?";

        try (
                Connection connection = ds.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setString(1, task.getDescription());
            ps.setInt(2, task.getPriority());

            // due_date (nullable)
            if (task.getDueDate() != null) {
                ps.setDate(3, java.sql.Date.valueOf(task.getDueDate()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            // completed_at (nullable)
            if (task.getCompletedAt() != null) {
                ps.setDate(4, java.sql.Date.valueOf(task.getCompletedAt()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            // project_id (nullable)
            if (task.getProjectIdObject() != null) {
                ps.setInt(5, task.getProjectId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }

            ps.setInt(6, task.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Update failed: task not found");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a task by the given task ID.
     * 
     * @param id the task ID to search for
     * @return the task object if found, null otherwise
     * @throws RuntimeException if an SQL exception occurs
     */
    @Override
    public Task getTaskById(int id) {
        String sql = "SELECT * FROM tasks WHERE tasks.id = ?";
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToTask(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm vào TaskDAOImpl.java
    public List<Task> getTasksDueOn(int userId, java.time.LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND due_date = ? AND completed_at IS NULL";

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setDate(2, java.sql.Date.valueOf(date));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs)); // Tận dụng hàm map có sẵn của bạn
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void completeTask(int id) {
        String sql = "UPDATE tasks SET completed_at = CURDATE() WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for tasks by title or description matching the query.
     *
     * @param userId the user ID to filter tasks
     * @param query the search query string
     * @return a list of tasks matching the search criteria
     */
    @Override
    public List<Task> searchTasks(int userId, String query) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND (title LIKE ? OR description LIKE ?) ORDER BY created_at DESC";

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            String searchPattern = "%" + query + "%";
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
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
}