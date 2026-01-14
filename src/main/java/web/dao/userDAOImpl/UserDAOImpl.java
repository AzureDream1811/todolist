package web.dao.userDAOImpl;

import web.dao.UserDAO;
import web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAOImpl implements UserDAO {

    private final DataSource ds;

    public UserDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Creates a new user with the given details.
     * Password is automatically hashed using BCrypt before storing.
     *
     * @param user the user to create
     * @return the created user if successful, null otherwise
     * @throws RuntimeException if an SQL exception occurs
     */
    public User createUser(User user) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Hash password using BCrypt
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            
            statement.setString(1, user.getUsername());
            statement.setString(2, hashedPassword);
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
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
     * Uses BCrypt to verify the password hash.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return true if the user is authenticated, false otherwise
     */
    public boolean authenticate(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            System.out.println("[AUTH] User not found: " + username);
            return false;
        }
        
        String storedPassword = user.getPassword();
        System.out.println("[AUTH] Username: " + username);
        System.out.println("[AUTH] Input password: '" + password + "'");
        System.out.println("[AUTH] Input password length: " + password.length());
        System.out.println("[AUTH] Input password bytes: " + java.util.Arrays.toString(password.getBytes()));
        System.out.println("[AUTH] Stored password length: " + storedPassword.length());
        System.out.println("[AUTH] Stored password: " + storedPassword);
        System.out.println("[AUTH] Is BCrypt format: " + storedPassword.startsWith("$2a$"));
        
        // Verify password using BCrypt
        try {
            boolean result = BCrypt.checkpw(password, storedPassword);
            System.out.println("[AUTH] BCrypt check result: " + result);
            return result;
        } catch (IllegalArgumentException e) {
            // Handle case where stored password is not a valid BCrypt hash (legacy plaintext)
            // For backward compatibility during migration
            System.err.println("[AUTH] Warning: User " + username + " has non-BCrypt password. Using plaintext comparison.");
            boolean result = storedPassword.equals(password);
            System.out.println("[AUTH] Plaintext check result: " + result);
            return result;
        }
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
        // populate avatar if present in the resultset
        try {
            String avatar = rs.getString("avatar");
            user.setAvatar(avatar);
        } catch (SQLException ignored) {
            // column might not exist in older schemas - ignore
        }
        return user;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws RuntimeException if an SQL exception occurs
     */
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

    /**
     * Demotes a user by their ID.
     * Sets the user's role to USER.
     *
     * @param id the ID of the user to demote
     * @throws RuntimeException if an SQL exception occurs
     */
    @Override
    public void demoteUser(int id) {
        String sql = "UPDATE users SET role = 'USER' WHERE users.id = ?";
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
     * Promotes a user by their ID.
     * Sets the user's role to ADMIN.
     * 
     * @param id the ID of the user to promote
     * @throws RuntimeException if an SQL exception occurs
     */
    @Override
    public void promoteUser(int id) {
        String sql = "UPDATE users SET role = 'ADMIN' WHERE users.id = ?";
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
     * Updates the avatar path for a user.
     *
     * @param userId the ID of the user
     * @param avatarPath the path to the avatar image
     */
    @Override
    public void updateAvatar(int userId, String avatarPath) {
        String sql = "UPDATE users SET avatar = ? WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, avatarPath);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email to search for
     * @return the user object if found, null otherwise
     */
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by email", e);
        }
    }

    /**
     * Updates the password for a user.
     *
     * @param userId the ID of the user
     * @param newPassword the new password
     */
    @Override
    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a password reset token for a user.
     *
     * @param userId the ID of the user
     * @param token the reset token
     */
    @Override
    public void saveResetToken(int userId, String token) {
        String sql = "UPDATE users SET reset_token = ?, reset_token_expiry = ? WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token);
            // Token expires in 1 hour
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis() + 3600000));
            statement.setInt(3, userId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a user by their reset token if it's still valid.
     *
     * @param token the reset token
     * @return the user object if found and token is valid, null otherwise
     */
    @Override
    public User getUserByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_token = ? AND reset_token_expiry > ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapResultSetToUser(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by reset token", e);
        }
    }

    /**
     * Clears the reset token for a user after password reset.
     *
     * @param userId the ID of the user
     */
    @Override
    public void clearResetToken(int userId) {
        String sql = "UPDATE users SET reset_token = NULL, reset_token_expiry = NULL WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}