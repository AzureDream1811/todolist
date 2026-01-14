USE todolist_db;

-- Admin (optional)
INSERT INTO users (username, email, password, created_at, role)
SELECT 'admin', 'admin@example.com', 'admin', CURRENT_DATE, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='admin');