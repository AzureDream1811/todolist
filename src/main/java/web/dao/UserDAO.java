package web.dao;

import web.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserDAO {
    User createUser(User user);
    User getUserByUsername(String username);
    boolean authenticate(String username, String password);
    User mapResultSetToUser(ResultSet rs) throws SQLException;
}