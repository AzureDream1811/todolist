USE todolist_db;

-- Users
INSERT INTO users (username, email, password, created_at, role) VALUES
('john_doe',    'john@example.com',  '1234', '2025-10-01', 'USER'),
('jane_smith',  'jane@example.com',  '1234', '2025-10-05', 'USER'),
('alice_wonder','alice@example.com', '1234', '2025-10-10', 'USER')
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Admin (optional)
INSERT INTO users (username, email, password, created_at, role)
SELECT 'admin', 'admin@example.com', 'admin', CURRENT_DATE, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='admin');

-- Projects (lookup user by username)
INSERT INTO projects (name, user_id, created_at)
SELECT 'Work Tasks', u.id, '2025-10-01' FROM users u WHERE u.username='john_doe';
INSERT INTO projects (name, user_id, created_at)
SELECT 'Personal', u.id, '2025-10-02' FROM users u WHERE u.username='john_doe';
INSERT INTO projects (name, user_id, created_at)
SELECT 'Shopping List', u.id, '2025-10-05' FROM users u WHERE u.username='jane_smith';
INSERT INTO projects (name, user_id, created_at)
SELECT 'Fitness Goals', u.id, '2025-10-10' FROM users u WHERE u.username='alice_wonder';

-- Tasks (lookup user_id + project_id by name)
INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at)
SELECT 'Complete project proposal', 'Write the initial draft of the project proposal', 1, '2025-11-15', NULL,
       u.id, p.id, '2025-10-10'
FROM users u JOIN projects p ON p.name='Work Tasks' AND p.user_id=u.id
WHERE u.username='john_doe';

INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at)
SELECT 'Review code', 'Code review for the new authentication module', 2, '2025-10-31', NULL,
       u.id, p.id, '2025-10-12'
FROM users u JOIN projects p ON p.name='Work Tasks' AND p.user_id=u.id
WHERE u.username='john_doe';

INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at)
SELECT 'Buy groceries', 'Milk, eggs, bread, and fruits', 3, '2025-10-31', NULL,
       u.id, p.id, '2025-10-28'
FROM users u JOIN projects p ON p.name='Personal' AND p.user_id=u.id
WHERE u.username='john_doe';

INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at)
SELECT 'Call mom', 'Wish her happy birthday', 1, '2025-10-25', '2025-10-20',
       u.id, p.id, '2025-10-20'
FROM users u JOIN projects p ON p.name='Personal' AND p.user_id=u.id
WHERE u.username='john_doe';

INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at)
SELECT 'Setup meeting', 'Schedule team meeting for next week', 2, '2025-11-01', NULL,
       u.id, p.id, '2025-10-27'
FROM users u JOIN projects p ON p.name='Work Tasks' AND p.user_id=u.id
WHERE u.username='john_doe';

-- User 2: one task without project_id (NULL)
INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at)
SELECT 'Return book to library', 'The Great Gatsby is due soon', 2, '2025-11-05', NULL,
       u.id, NULL, '2025-10-20'
FROM users u WHERE u.username='jane_smith';
