-- Add instructor_email column (nullable, 120 chars)
ALTER TABLE course ADD COLUMN instructor_email VARCHAR(120);
