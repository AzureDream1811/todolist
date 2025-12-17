package web.dao.userDAOImpl;

import web.dao.UserDAO;
import web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class UserDAOImpl implements UserDAO {

    private final DataSource ds;

    public UserDAOImpl(DataSource ds) {
        this.ds = ds;
    }

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
            Connection connection = ds.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0)
                return null;

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
            Connection connection = ds.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by username", e);
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE users.id = ?";
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                return resultSet.next() ? mapResultSetToUser(resultSet) : null;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error fetching user by id", e);

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
     * Retrieves a list of all users in the database.
     *
     * @return a list of all users in the database
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
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
        Date created = rs.getDate("created_at");
        if (created != null) {
            user.setCreatedAt(created.toLocalDate());
        }
        // populate role if present in the resultset
        try {
            String role = rs.getString("role");
            if (role != null)
                user.setRole(role);
        } catch (SQLException ignored) {
            // column might not exist in older schemas - ignore
        }
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        String sql = "DELETE FROM users WHERE users.id = ?";
        try (Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}