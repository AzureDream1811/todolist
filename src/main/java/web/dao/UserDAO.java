package web.dao;

import web.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User createUser(User user);

    User getUserByUsername(String username);

    User getUserById(int id);

    User getUserByEmail(String email);

    boolean authenticate(String username, String password);

    List<User> getAllUsers();

    void deleteUserById(int id);

    void demoteUser(int id);

    void promoteUser(int id);

    void updateAvatar(int userId, String avatarPath);

    void updatePassword(int userId, String newPassword);

    // Reset token methods
    void saveResetToken(int userId, String token);

    User getUserByResetToken(String token);

    void clearResetToken(int userId);

    User mapResultSetToUser(ResultSet rs) throws SQLException;
}