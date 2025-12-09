package web.dao;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import web.dao.projectDAOImpl.ProjectDAOImpl;
import web.dao.taskDAOImpl.TaskDAOImpl;
import web.dao.userDAOImpl.UserDAOImpl;

public class DAOFactory {
    private static DAOFactory instance = new DAOFactory();
    private final DataSource dataSource;

    private DAOFactory() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/todolist_db");
        ds.setUser("root");
        ds.setPassword("1234");
        this.dataSource = ds;
    }

    public static DAOFactory getInstance() {
        return instance;
    }

    public UserDAO getUserDAO() {
        return new UserDAOImpl(dataSource);
    }

    public TaskDAO getTaskDAO() {
        return new TaskDAOImpl(dataSource);
    }

    public ProjectDAO getProjectDAO() {
        return new ProjectDAOImpl(dataSource);
    }

}
