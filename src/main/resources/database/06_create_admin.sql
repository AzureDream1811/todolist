-- Create admin account with BCrypt hashed password
-- Default password: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

USE todolist_db;

-- Insert admin if not exists
INSERT INTO users (username, email, password, created_at, role)
SELECT 'admin', 'admin@todolist.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', CURRENT_DATE, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='admin');

-- Verify admin created
SELECT id, username, email, role, created_at 
FROM users 
WHERE role = 'ADMIN';
