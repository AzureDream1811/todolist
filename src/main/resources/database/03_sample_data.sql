USE todolist_db;

-- =========================
-- Sample Users (khớp schema)
-- =========================
INSERT INTO users (username, email, password, created_at, role) VALUES
('john_doe',    'john@example.com',  '1234', '2025-10-01', 'USER'),
('jane_smith',  'jane@example.com',  '1234', '2025-10-05', 'USER'),
('alice_wonder','alice@example.com', '1234', '2025-10-10', 'USER');

-- (Tuỳ chọn) nếu bạn muốn đảm bảo có admin seed
INSERT INTO users (username, email, password, created_at, role)
SELECT 'admin', 'admin@example.com', 'admin', CURRENT_DATE, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='admin');


-- =========================
-- Sample Projects
-- Lưu ý: user_id dựa theo AUTO_INCREMENT (1..3) nếu DB mới tinh
-- =========================
INSERT INTO projects (name, user_id, created_at) VALUES
('Work Tasks',      1, '2025-10-01'),
('Personal',        1, '2025-10-02'),
('Shopping List',   2, '2025-10-05'),
('Fitness Goals',   3, '2025-10-10');


-- =========================
-- Sample Tasks (đổi completed -> completed_at)
-- completed_at NULL => chưa hoàn thành
-- completed_at có ngày => đã hoàn thành
-- =========================

-- User 1's tasks
INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at) VALUES
('Complete project proposal', 'Write the initial draft of the project proposal', 1, '2025-11-15', NULL,         1, 1, '2025-10-10'),
('Review code',               'Code review for the new authentication module',   2, '2025-10-31', NULL,         1, 1, '2025-10-12'),
('Buy groceries',             'Milk, eggs, bread, and fruits',                   3, '2025-10-31', NULL,         1, 2, '2025-10-28'),
('Call mom',                  'Wish her happy birthday',                         1, '2025-10-25', '2025-10-20', 1, 2, '2025-10-20'),
('Setup meeting',             'Schedule team meeting for next week',             2, '2025-11-01', NULL,         1, 1, '2025-10-27');

-- User 2's tasks
INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at) VALUES
('Buy new laptop',         'Research and purchase a new laptop',                 1, '2025-11-10', NULL,         2, 3,    '2025-10-15'),
('Return book to library', 'The Great Gatsby is due soon',                       2, '2025-11-05', NULL,         2, NULL, '2025-10-20'),
('Plan weekend trip',      'Book hotel and plan itinerary',                      3, '2025-10-20', '2025-10-18', 2, 3,    '2025-10-18');

-- User 3's tasks
INSERT INTO tasks (title, description, priority, due_date, completed_at, user_id, project_id, created_at) VALUES
('Morning run',  '5km run in the park',                                          2, '2025-10-31', NULL,         3, 4, '2025-10-29'),
('Meal prep',    'Prepare meals for the week',                                    1, '2025-11-02', NULL,         3, 4, '2025-10-30'),
('Yoga session', 'Evening yoga class',                                            3, '2025-10-29', '2025-10-28', 3, 4, '2025-10-28');
