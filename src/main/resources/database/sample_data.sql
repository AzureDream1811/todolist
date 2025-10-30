-- Sample data for todo list application

-- Insert sample users
INSERT INTO users (username, email, password, created_at) VALUES
('john_doe', 'john@example.com', '1234', '2025-10-01'),
('jane_smith', 'jane@example.com', '1234', '2025-10-05'),
('alice_wonder', 'alice@example.com', '1234', '2025-10-10');

-- Insert sample projects
INSERT INTO projects (name, user_id, created_at) VALUES
('Work Tasks', 1, '2025-10-01'),
('Personal', 1, '2025-10-02'),
('Shopping List', 2, '2025-10-05'),
('Fitness Goals', 3, '2025-10-10');

-- Insert sample tasks
-- User 1's tasks
INSERT INTO tasks (title, description, completed, priority, due_date, user_id, project_id, created_at) VALUES
('Complete project proposal', 'Write the initial draft of the project proposal', FALSE, 1, '2025-11-15', 1, 1, '2025-10-10'),
('Review code', 'Code review for the new authentication module', FALSE, 2, '2025-10-31', 1, 1, '2025-10-12'),
('Buy groceries', 'Milk, eggs, bread, and fruits', FALSE, 3, '2025-10-31', 1, 2, '2025-10-28'),
('Call mom', 'Wish her happy birthday', TRUE, 1, '2025-10-25', 1, 2, '2025-10-20'),
('Setup meeting', 'Schedule team meeting for next week', FALSE, 2, '2025-11-01', 1, 1, '2025-10-27');

-- User 2's tasks
INSERT INTO tasks (title, description, completed, priority, due_date, user_id, project_id, created_at) VALUES
('Buy new laptop', 'Research and purchase a new laptop', FALSE, 1, '2025-11-10', 2, 3, '2025-10-15'),
('Return book to library', 'The Great Gatsby is due soon', FALSE, 2, '2025-11-05', 2, NULL, '2025-10-20'),
('Plan weekend trip', 'Book hotel and plan itinerary', TRUE, 3, '2025-10-20', 2, 3, '2025-10-18');

-- User 3's tasks
INSERT INTO tasks (title, description, completed, priority, due_date, user_id, project_id, created_at) VALUES
('Morning run', '5km run in the park', FALSE, 2, '2025-10-31', 3, 4, '2025-10-29'),
('Meal prep', 'Prepare meals for the week', FALSE, 1, '2025-11-02', 3, 4, '2025-10-30'),
('Yoga session', 'Evening yoga class', TRUE, 3, '2025-10-29', 3, 4, '2025-10-28');

-- Note about passwords:
-- In a real application, never store plain text passwords!
-- The password hashes above are just placeholders and should be replaced with
-- properly hashed passwords using a secure hashing algorithm like bcrypt.
-- Example of a bcrypt hash for 'password123': $2a$10$N9qo8uLOickgx2ZMRZoMy.MQDqShCs6UdFWwqWhHXaV3xJ1I5TjLu
