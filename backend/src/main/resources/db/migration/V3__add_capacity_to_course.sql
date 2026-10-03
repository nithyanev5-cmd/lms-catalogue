-- Add capacity column with a safe default and enforce NOT NULL
ALTER TABLE course ADD COLUMN capacity INT DEFAULT 30;
UPDATE course SET capacity = 30 WHERE capacity IS NULL;
ALTER TABLE course MODIFY COLUMN capacity INT NOT NULL DEFAULT 30;
