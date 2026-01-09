package web.dao;

import web.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User createUser(User user);

    User getUserByUsername(String username);

    User getUserById(int id);

    boolean authenticate(String username, String password);

    List<User> getAllUsers();

    void deleteUserById(int id);

    void demoteUser(int id);

    void promoteUser(int id);

    void updateAvatar(int userId, String avatarPath);

    User mapResultSetToUser(ResultSet rs) throws SQLException;
}