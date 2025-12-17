package web.dao;

import web.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User createUser(User user);
    User getUserByUsername(String username);
    boolean authenticate(String username, String password);
    List<User> getAllUsers();
    User mapResultSetToUser(ResultSet rs) throws SQLException;
}