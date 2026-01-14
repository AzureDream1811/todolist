-- Check password hash lengths
USE todolist_db;

SELECT 
    username, 
    LENGTH(password) as hash_length,
    password
FROM users 
WHERE username = 'AzureDream';
