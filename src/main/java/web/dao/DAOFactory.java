package web.dao;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import web.dao.projectDAOImpl.ProjectDAOImpl;
import web.dao.taskDAOImpl.TaskDAOImpl;
import web.dao.userDAOImpl.UserDAOImpl;

/**
 * Factory class for creating DAO instances with connection pooling support.
 * Uses HikariCP for efficient database connection management.
 */
public class DAOFactory {
    private static DAOFactory instance = new DAOFactory();
    private final DataSource dataSource;

    private DAOFactory() {
        Properties props = new Properties();

        try (InputStream input = DAOFactory.class.getClassLoader().getResourceAsStream("database.properties")) {

            props.load(input);

            // Configure HikariCP Connection Pool
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            
            // Pool configuration for optimal performance
            config.setMaximumPoolSize(10);              // Max 10 connections
            config.setMinimumIdle(2);                   // Min 2 idle connections
            config.setConnectionTimeout(30000);         // 30 seconds timeout
            config.setIdleTimeout(600000);              // 10 minutes idle timeout
            config.setMaxLifetime(1800000);             // 30 minutes max lifetime
            
            // Performance optimizations
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            
            this.dataSource = new HikariDataSource(config);
            
            System.out.println("✅ HikariCP Connection Pool initialized successfully");

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
    
    /**
     * Closes the connection pool. Should be called on application shutdown.
     */
    public void shutdown() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            System.out.println("✅ HikariCP Connection Pool closed");
        }
    }

}
