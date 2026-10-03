-- V8__create_course_prerequisite.sql
-- Create join table to record prerequisite relationships between courses.
-- Each row represents: course requires prerequisite_course
-- Composite PK enforces uniqueness; CHECK prevents self-reference.

CREATE TABLE course_prerequisite (
  course_id BIGINT NOT NULL,
  prerequisite_course_id BIGINT NOT NULL,
  PRIMARY KEY (course_id, prerequisite_course_id),
  CONSTRAINT fk_cp_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE RESTRICT,
  CONSTRAINT fk_cp_prereq_course FOREIGN KEY (prerequisite_course_id) REFERENCES course(id) ON DELETE RESTRICT,
  CONSTRAINT chk_cp_not_self CHECK (course_id <> prerequisite_course_id)
);

-- Index to speed lookups by prerequisite_course_id (useful for reverse-lookup)
CREATE INDEX ix_course_prerequisite_prereq ON course_prerequisite (prerequisite_course_id);
