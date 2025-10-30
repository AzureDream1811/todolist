# Todolist

A simple task management application built with Java Servlets.

## Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Apache Tomcat 10.x
- Maven 3.8+

### Database Setup
1. Create a MySQL database:
   ```sql
   CREATE DATABASE todolist_db;
   ```

2. Run the database schema and sample data:
   ```sql
   -- Run schema.sql first
   -- Then run sample_data.sql if you want sample data
   ```

### Environment Setup
1. Copy the example environment file:
   ```bash
   # Windows
   copy .env.example .env
   
   # Linux/macOS
   cp .env.example .env
   ```

2. Edit `.env` with your database credentials:
   ```
   DB_URL=jdbc:mysql://localhost:3306/todolist_db
   DB_USER=your_username
   DB_PASSWORD=your_password
   ```

### Building the Application
1. Build the WAR file:
   ```bash
   mvn clean package
   ```

### Deploying to Tomcat
1. Copy the generated `target/todolist.war` to Tomcat's `webapps` directory
2. Start Tomcat server
3. Access the application at: `http://localhost:8080/todolist`

## Development
- Always use `.env` for local configuration
- Never commit `.env` to version control
- Update `.env.example` if new environment variables are added
- The application uses Java Servlets with a simple MVC pattern

## Project Structure
- `src/main/java/web` - Servlet controllers and business logic
- `src/main/webapp` - JSP views and static resources
- `src/main/resources` - Configuration files
- `src/test` - Test files