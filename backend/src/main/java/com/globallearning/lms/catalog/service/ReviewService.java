package com.globallearning.lms.catalog.service;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.domain.Review;
import com.globallearning.lms.catalog.repository.CourseRepository;
import com.globallearning.lms.catalog.repository.EnrollmentRepository;
import com.globallearning.lms.catalog.repository.ReviewRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseService courseService;
    private final LearnerService learnerService;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public ReviewService(
        ReviewRepository reviewRepository,
        CourseService courseService,
        LearnerService learnerService,
        EnrollmentRepository enrollmentRepository,
        CourseRepository courseRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.courseService = courseService;
        this.learnerService = learnerService;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Review create(String courseCode, String learnerRef, Integer rating, String comment) {
        // Validate inputs
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (comment != null && comment.length() > 1000) {
            throw new IllegalArgumentException("Comment must not exceed 1000 characters");
        }

        // Get course and learner
        Course course = courseService.getByCode(courseCode);
        Learner learner = learnerService.getByRef(learnerRef);

        // Check for archived course
        if (course.getArchivedAt() != null) {
            throw new CourseArchivedException(courseCode);
        }

        // Verify enrollment
        Enrollment enrollment = enrollmentRepository.findByCourseAndLearnerAndStatus(
            course, learner, EnrollmentStatus.ACTIVE
        ).orElseThrow(() -> new NotEnrolledException(courseCode, learnerRef));

        // Check for duplicate review
        if (reviewRepository.findByCourseAndLearner(course, learner).isPresent()) {
            throw new DuplicateReviewException(courseCode, learnerRef);
        }

        // Create review
        Review review = new Review(course, learner, rating, comment);
        Review saved = reviewRepository.save(review);

        // Update course aggregates
        updateCourseAggregates(course);

        return saved;
    }

    @Transactional
    public Review update(String courseCode, String learnerRef, Integer rating, String comment) {
        // Validate inputs
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (comment != null && comment.length() > 1000) {
            throw new IllegalArgumentException("Comment must not exceed 1000 characters");
        }

        // Get course and learner
        Course course = courseService.getByCode(courseCode);
        Learner learner = learnerService.getByRef(learnerRef);

        // Check for archived course
        if (course.getArchivedAt() != null) {
            throw new CourseArchivedException(courseCode);
        }

        // Verify enrollment
        enrollmentRepository.findByCourseAndLearnerAndStatus(
            course, learner, EnrollmentStatus.ACTIVE
        ).orElseThrow(() -> new NotEnrolledException(courseCode, learnerRef));

        // Get existing review
        Review review = reviewRepository.findByCourseAndLearner(course, learner)
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, learnerRef));

        // Update review
        review.setRating(rating);
        review.setComment(comment);
        Review updated = reviewRepository.save(review);

        // Update course aggregates
        updateCourseAggregates(course);

        return updated;
    }

    @Transactional
    public void delete(String courseCode, String learnerRef) {
        // Get course and learner
        Course course = courseService.getByCode(courseCode);
        Learner learner = learnerService.getByRef(learnerRef);

        // Verify enrollment exists (optional per spec, but good validation)
        enrollmentRepository.findByCourseAndLearnerAndStatus(
            course, learner, EnrollmentStatus.ACTIVE
        ).orElseThrow(() -> new NotEnrolledException(courseCode, learnerRef));

        // Get and delete review
        Review review = reviewRepository.findByCourseAndLearner(course, learner)
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, learnerRef));

        reviewRepository.delete(review);

        // Update course aggregates
        updateCourseAggregates(course);
    }

    @Transactional(readOnly = true)
    public List<Review> listForCourse(String courseCode) {
        Course course = courseService.getByCode(courseCode);
        return reviewRepository.findByCourseOrderByCreatedAtDesc(course);
    }

    @Transactional(readOnly = true)
    public Review getForCourseByLearner(String courseCode, String learnerRef) {
        Course course = courseService.getByCode(courseCode);
        Learner learner = learnerService.getByRef(learnerRef);

        return reviewRepository.findByCourseAndLearner(course, learner)
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, learnerRef));
    }

    private void updateCourseAggregates(Course course) {
        List<Review> reviews = reviewRepository.findByCourseOrderByCreatedAtDesc(course);

        if (reviews.isEmpty()) {
            course.setReviewCount(0);
            course.setAverageRating(null);
        } else {
            int count = reviews.size();
            double sum = reviews.stream().mapToInt(Review::getRating).sum();
            double average = sum / count;
            
            course.setReviewCount(count);
            course.setAverageRating(BigDecimal.valueOf(average).setScale(2, java.math.RoundingMode.HALF_UP).doubleValue());
        }

        courseRepository.save(course);
    }
}
