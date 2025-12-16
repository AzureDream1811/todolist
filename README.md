**Todolist — Run Instructions**

- **Description:**: Simple Java Maven webapp (WAR) — a lightweight to‑do list application.

**Prerequisites**
- **Java:**: JDK 21 (project uses `maven.compiler.source/target=21` in `pom.xml`).
- **Maven:**: Maven 3.6+ to build the project.
- **Database:**: MySQL server (local or reachable) to host `todolist_db`.
- **Servlet container (deployment):**: Apache Tomcat 9/10 or run from an IDE that supports Java webapps.

**Database setup**
- **Start MySQL** and run the SQL scripts in the `src/main/resources/database` folder in order.

From the project root run (use a shell where `mysql` client is available):

```bash
mysql -u root -p < src/main/resources/database/01_admin_setup.sql.sql
mysql -u root -p < src/main/resources/database/02_schema.sql.sql
mysql -u root -p < src/main/resources/database/03_sample_data.sql
```

- The project includes a default DB config at [src/main/resources/database.properties](src/main/resources/database.properties) with:
- **DB URL:** jdbc:mysql://localhost:3306/todolist_db
- **DB user/password:** todolist / 1234

If you change the DB credentials or host, update [src/main/resources/database.properties](src/main/resources/database.properties) accordingly.

**Build**
- From the project root run:

```bash
mvn clean package
```

- The build produces `target/todolist.war`.

**Run / Deploy**
- Option A — Deploy to Tomcat:
  - Copy `target/todolist.war` to your Tomcat `webapps/` folder.
  - Start Tomcat and open: `http://localhost:8080/todolist/`

- Option B — Run from an IDE:
  - Import the project as a Maven project in IntelliJ/STS/Eclipse.
  - Configure a local Tomcat server and run the webapp.

**Default credentials / notes**
- Sample data includes a seeded admin account (username: `admin`, password: `admin`) — change this in production.
- Passwords in sample data are plaintext for convenience; enable hashing (BCrypt/Argon2) before production use.

**Common issues & troubleshooting**
- If you see JDBC connection errors: ensure MySQL is running and `database.properties` matches server settings.
- Port conflicts: Tomcat default is `8080` — change if needed in Tomcat config.
- If views show `null` fields, ensure SQL scripts ran successfully and the `users`/`tasks`/`projects` tables exist.

**Helpful file references**
- Schema & seeds: [src/main/resources/database/02_schema.sql.sql](src/main/resources/database/02_schema.sql.sql), [src/main/resources/database/03_sample_data.sql](src/main/resources/database/03_sample_data.sql)
- DB config: [src/main/resources/database.properties](src/main/resources/database.properties)
- Main servlets: `AppServlet` ([src/main/java/web/controller/AppServlet.java](src/main/java/web/controller/AppServlet.java)) and `AuthServlet` ([src/main/java/web/controller/AuthServlet.java](src/main/java/web/controller/AuthServlet.java)).

If you want, I can add a small script to run the SQL files automatically or add a Maven profile that runs the DB setup.
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
### Environment Setup

This project currently does NOT load `.env` automatically. The application reads database configuration from
`src/main/resources/database.properties` by default. Update that file when running locally or deploy-time configuration.

If you prefer environment variables instead of `database.properties`, you can set `DB_URL`, `DB_USER`, and
`DB_PASSWORD` at the OS/Tomcat level and modify the code to read `System.getenv(...)`. `.env` is provided for
convenience only and is ignored by the application unless you add a dotenv loader.

Examples (optional) — set OS env vars instead of using `.env`:

Windows PowerShell (session only):
```powershell
$Env:DB_URL = "jdbc:mysql://localhost:3306/todolist_db"
$Env:DB_USER = "todolist"
$Env:DB_PASSWORD = "1234"
```

Linux/macOS (session only):
```bash
export DB_URL="jdbc:mysql://localhost:3306/todolist_db"
export DB_USER="todolist"
export DB_PASSWORD="1234"
```

Keep `.env` in `.gitignore` (already configured). If you want, I can update the app to prefer environment variables with a fallback to
`database.properties` or add a dotenv loader to read `.env` automatically.

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