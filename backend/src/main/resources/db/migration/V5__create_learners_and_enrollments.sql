-- Create learner table
CREATE TABLE learner (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  learner_ref VARCHAR(32) NOT NULL UNIQUE,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  email VARCHAR(120),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create enrollment table
CREATE TABLE enrollment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_id BIGINT NOT NULL,
  learner_id BIGINT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES course(id),
  CONSTRAINT fk_enrollment_learner FOREIGN KEY (learner_id) REFERENCES learner(id)
);

-- Seed 10 learners
INSERT INTO learner (learner_ref, first_name, last_name, email, created_at) VALUES
( 'L-00001', 'Learner', 'One', 'learner1@example.com', NOW()),
( 'L-00002', 'Learner', 'Two', 'learner2@example.com', NOW()),
( 'L-00003', 'Learner', 'Three', 'learner3@example.com', NOW()),
( 'L-00004', 'Learner', 'Four', 'learner4@example.com', NOW()),
( 'L-00005', 'Learner', 'Five', 'learner5@example.com', NOW()),
( 'L-00006', 'Learner', 'Six', 'learner6@example.com', NOW()),
( 'L-00007', 'Learner', 'Seven', 'learner7@example.com', NOW()),
( 'L-00008', 'Learner', 'Eight', 'learner8@example.com', NOW()),
( 'L-00009', 'Learner', 'Nine', 'learner9@example.com', NOW()),
( 'L-00010', 'Learner', 'Ten', 'learner10@example.com', NOW());
