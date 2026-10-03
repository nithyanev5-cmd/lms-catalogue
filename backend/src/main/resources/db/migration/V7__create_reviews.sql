-- Flyway migration: Create reviews table and add denormalized columns to course

CREATE TABLE review (
  id BIGINT NOT NULL AUTO_INCREMENT,
  course_id BIGINT NOT NULL,
  learner_id BIGINT NOT NULL,
  rating INT NOT NULL,
  comment VARCHAR(1000) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT uq_review_course_learner UNIQUE (course_id, learner_id),
  CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5),
  CONSTRAINT fk_review_course FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
  CONSTRAINT fk_review_learner FOREIGN KEY (learner_id) REFERENCES learner(id) ON DELETE CASCADE
);

CREATE INDEX ix_review_course_id ON review(course_id);

-- Denormalized aggregates for fast reads
ALTER TABLE course
  ADD COLUMN average_rating DOUBLE DEFAULT NULL,
  ADD COLUMN review_count INT NOT NULL DEFAULT 0;
