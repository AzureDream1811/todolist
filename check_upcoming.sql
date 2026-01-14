-- Check upcoming tasks
USE todolist_db;

SELECT 
    id,
    title,
    due_date,
    completed_at,
    user_id,
    CASE 
        WHEN due_date IS NULL THEN 'NO DUE DATE'
        WHEN due_date > CURDATE() THEN 'UPCOMING'
        WHEN due_date = CURDATE() THEN 'TODAY'
        WHEN due_date < CURDATE() THEN 'OVERDUE'
    END as status
FROM tasks
WHERE completed_at IS NULL
ORDER BY due_date;
