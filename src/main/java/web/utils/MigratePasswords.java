package web.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Utility to migrate existing plaintext passwords to BCrypt hashes.
 * Run this ONCE after deploying BCrypt changes.
 * 
 * WARNING: Make sure to backup database before running!
 * 
 * Usage: Run this as a standalone Java application
 */
public class MigratePasswords {
    
    public static void main(String[] args) {
        System.out.println("=== Password Migration Utility ===");
        System.out.println("This will hash all plaintext passwords in the database.");
        System.out.println();
        
        try {
            migrateAllPasswords();
            System.out.println("✅ Migration completed successfully!");
        } catch (Exception e) {
            System.err.println("❌ Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void migrateAllPasswords() throws Exception {
        // Load database properties
        Properties props = new Properties();
        try (InputStream input = MigratePasswords.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            props.load(input);
        }
        
        // Create DataSource
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));
        
        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            String selectSql = "SELECT id, username, password FROM users WHERE password NOT LIKE '$2a$%'";
            String updateSql = "UPDATE users SET password = ? WHERE id = ?";
            
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                 PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                
                ResultSet rs = selectStmt.executeQuery();
                int count = 0;
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String plaintextPassword = rs.getString("password");
                    
                    // Hash the plaintext password
                    String hashedPassword = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
                    
                    // Update in database
                    updateStmt.setString(1, hashedPassword);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                    
                    count++;
                    System.out.println("✅ Migrated password for user: " + username);
                }
                
                System.out.println();
                System.out.println("Total users migrated: " + count);
            }
        }
    }
}
