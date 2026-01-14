-- Check all users
USE todolist_db;

SELECT id, username, email, role
FROM users
ORDER BY id;
