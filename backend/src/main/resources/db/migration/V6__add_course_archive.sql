ALTER TABLE course
  ADD COLUMN archived_at TIMESTAMP NULL;

ALTER TABLE course
  ADD COLUMN archive_reason VARCHAR(500) NULL;

CREATE INDEX ix_course_archived_at ON course (archived_at);
