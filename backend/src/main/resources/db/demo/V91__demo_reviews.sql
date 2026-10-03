-- ---------------------------------------------------------------------------
-- Demo reviews.
--
-- The review API shipped on Day 5 with no data behind it. Every course in the
-- catalogue reports averageRating: null and reviewCount: 0, which makes a
-- rating badge impossible to look at and an average impossible to watch move.
--
-- This seeds two courses and deliberately leaves the rest empty:
--
--   WEB2208   8 reviews, average 4.63   (L-00034 .. L-00041)
--   CS3401    3 reviews, average 3.67   (L-00001, L-00002, L-00003)
--
-- Everything else stays unrated, so the "no ratings yet" branch is always
-- reachable from the catalogue screen.
--
-- Every reviewer here holds an ACTIVE enrolment on the course being reviewed,
-- which the service layer requires. Learners L-00042 .. L-00048 are enrolled
-- on WEB2208 and left unreviewed on purpose: they are the spare identities for
-- live demonstrations and for repeated test runs.
--
-- L-00005 is skipped on CS3401. It holds two ACTIVE enrolment rows from
-- V90 and any review operation for that pair fails before it reaches the
-- database.
-- ---------------------------------------------------------------------------

INSERT INTO review (course_id, learner_id, rating, comment, created_at)
SELECT c.id, l.id, r.rating, r.comment, r.created_at
  FROM course c
  JOIN (
        SELECT 'L-00034' AS learner_ref, 5 AS rating, 'Best module of the year. The component exercises actually stuck.' AS comment, TIMESTAMP('2026-03-02 09:14:00') AS created_at
  UNION SELECT 'L-00035', 5, 'Clear, well paced, and the tutor answered everything.',                              TIMESTAMP('2026-03-04 17:41:00')
  UNION SELECT 'L-00036', 4, 'Strong content. The first two weeks move very quickly.',                             TIMESTAMP('2026-03-09 11:02:00')
  UNION SELECT 'L-00037', 5, NULL,                                                                                 TIMESTAMP('2026-03-11 08:55:00')
  UNION SELECT 'L-00038', 5, 'Genuinely changed how I structure a front end.',                                     TIMESTAMP('2026-03-15 20:08:00')
  UNION SELECT 'L-00039', 4, 'Good, though the assessment brief could be clearer.',                                TIMESTAMP('2026-03-19 13:27:00')
  UNION SELECT 'L-00040', 5, 'Would take it again.',                                                               TIMESTAMP('2026-03-23 15:30:00')
  UNION SELECT 'L-00041', 4, NULL,                                                                                 TIMESTAMP('2026-03-28 10:12:00')
       ) AS r
  JOIN learner l ON l.learner_ref = r.learner_ref
 WHERE c.code = 'WEB2208';

INSERT INTO review (course_id, learner_id, rating, comment, created_at)
SELECT c.id, l.id, r.rating, r.comment, r.created_at
  FROM course c
  JOIN (
        SELECT 'L-00001' AS learner_ref, 4 AS rating, 'Hard but fair. Consensus week is worth the pain.' AS comment, TIMESTAMP('2026-02-11 14:20:00') AS created_at
  UNION SELECT 'L-00002', 3, 'Heavy on theory, light on practice.',                                                  TIMESTAMP('2026-02-18 09:47:00')
  UNION SELECT 'L-00003', 4, NULL,                                                                                   TIMESTAMP('2026-02-26 16:03:00')
       ) AS r
  JOIN learner l ON l.learner_ref = r.learner_ref
 WHERE c.code = 'CS3401';

-- ---------------------------------------------------------------------------
-- The aggregates.
--
-- course.average_rating and course.review_count are maintained by
-- ReviewService inside the transaction that writes a review. They are not
-- computed on read and there is no trigger. A seed that inserts review rows
-- without updating these two columns leaves the API reporting zero reviews on
-- a course that visibly has them.
--
-- ROUND(..., 2) matches the HALF_UP scale-2 rounding the service applies.
-- ---------------------------------------------------------------------------

UPDATE course c
   SET c.review_count   = (SELECT COUNT(*)             FROM review r WHERE r.course_id = c.id),
       c.average_rating = (SELECT ROUND(AVG(r.rating), 2) FROM review r WHERE r.course_id = c.id)
 WHERE c.code IN ('WEB2208', 'CS3401');
