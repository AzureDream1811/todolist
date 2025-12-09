package web.dao;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import web.dao.projectDAOImpl.ProjectDAOImpl;
import web.dao.taskDAOImpl.TaskDAOImpl;
import web.dao.userDAOImpl.UserDAOImpl;

public class DAOFactory {
    private static DAOFactory instance = new DAOFactory();
    private final DataSource dataSource;

    private DAOFactory() {
        Properties props = new Properties();

        try (InputStream input = DAOFactory.class.getClassLoader().getResourceAsStream("database.properties")) {

            props.load(input);

            MysqlDataSource ds = new MysqlDataSource();
            ds.setURL(props.getProperty("db.url"));
            ds.setUser(props.getProperty("db.user"));
            ds.setPassword(props.getProperty("db.password"));
            this.dataSource = ds;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB configuration", e);
        }
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
