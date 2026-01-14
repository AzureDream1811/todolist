-- Add reset token columns for password reset functionality
-- Run these statements one by one. If column already exists, it will show an error (safe to ignore)

ALTER TABLE users ADD COLUMN reset_token VARCHAR(255) DEFAULT NULL;
ALTER TABLE users ADD COLUMN reset_token_expiry DATETIME DEFAULT NULL;

-- Add index for faster token lookup
CREATE INDEX idx_users_reset_token ON users(reset_token);
