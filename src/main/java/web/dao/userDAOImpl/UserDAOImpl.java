package web.dao.userDAOImpl;

import web.dao.UserDAO;
import web.model.User;
import web.utils.DatabaseUtils;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    /**
     * Creates a new user with the given details.
     *
     * @param user the user to create
     * @return the created user if successful, null otherwise
     * @throws RuntimeException if an SQL exception occurs
     */
    public User createUser(User user) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(2, user.getEmail());


            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) return null;

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return the user object if found, null otherwise
     * @throws RuntimeException if an SQL exception occurs
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            Connection connection = DatabaseUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by username", e);
        }
    }

    /**
     * Authenticates a user by checking their username and password.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return true if the user is authenticated, false otherwise
     */
    public boolean authenticate(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Maps a given ResultSet to a User object.
     *
     * @param rs the ResultSet containing the user data
     * @return the mapped User object
     * @throws SQLException if an SQL exception occurs
     */
    public User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getDate("created_at").toLocalDate());
        return user;
    }
}