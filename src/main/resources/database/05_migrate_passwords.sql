-- Migration script: Hash existing plaintext passwords
-- Run this ONCE after deploying BCrypt changes

-- WARNING: This will hash all current plaintext passwords
-- Make sure to backup the database before running!

-- For MySQL, we cannot directly hash in SQL, so you have 2 options:

-- OPTION 1: Reset all passwords to a default hashed value (for testing)
-- Default password: "password123" 
-- BCrypt hash of "password123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE password NOT LIKE '$2a$%'; -- Only update non-BCrypt passwords

-- After running this, all users can login with password: "password123"
-- Then ask users to change their password


-- OPTION 2: Create a Java migration utility
-- See: MigratePasswords.java in src/main/java/web/utils/
