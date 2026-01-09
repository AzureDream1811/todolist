USE todolist_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    avatar VARCHAR(255) DEFAULT NULL,
    created_at DATE DEFAULT (CURRENT_DATE),
	role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER'
);


-- Projects table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_name (name)
);

-- Tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority TINYINT NOT NULL DEFAULT 2 CHECK (priority BETWEEN 1 AND 3),
    due_date DATE DEFAULT NULL,
    completed_at DATE NULL,
    project_id BIGINT NULL,
    user_id BIGINT NOT NULL,
    created_at DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_title (title),
    INDEX idx_priority (priority),
    INDEX idx_due_date (due_date),
    INDEX idx_completed_at (completed_at),
    INDEX idx_user (user_id),
	INDEX idx_user_completed (user_id, completed_at),
	INDEX idx_project (project_id)
);
