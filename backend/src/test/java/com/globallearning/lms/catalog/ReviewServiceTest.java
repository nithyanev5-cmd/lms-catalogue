package com.globallearning.lms.catalog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.domain.Review;
import com.globallearning.lms.catalog.repository.CourseRepository;
import com.globallearning.lms.catalog.repository.EnrollmentRepository;
import com.globallearning.lms.catalog.repository.ReviewRepository;
import com.globallearning.lms.catalog.service.CourseNotFoundException;
import com.globallearning.lms.catalog.service.CourseService;
import com.globallearning.lms.catalog.service.LearnerNotFoundException;
import com.globallearning.lms.catalog.service.LearnerService;
import com.globallearning.lms.catalog.service.ReviewService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private LearnerService learnerService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reviewService = new ReviewService(
            reviewRepository,
            courseService,
            learnerService,
            enrollmentRepository,
            courseRepository
        );
    }

    @Test
    void createReviewThrowsWhenCourseNotFound() {
        given(courseService.getByCode("NONEXISTENT"))
            .willThrow(new CourseNotFoundException("NONEXISTENT"));

        assertThrows(CourseNotFoundException.class, () -> {
            reviewService.create("NONEXISTENT", "LRN-001", 5, "Great course");
        });
    }

    @Test
    void createReviewThrowsWhenLearnerNotFound() {
        Course course = new Course("CS1007", "Foundations", "Basic.", CourseLevel.FOUNDATION, 10, true);
        given(courseService.getByCode("CS1007")).willReturn(course);
        given(learnerService.getByRef("NONEXISTENT"))
            .willThrow(new LearnerNotFoundException("NONEXISTENT"));

        assertThrows(LearnerNotFoundException.class, () -> {
            reviewService.create("CS1007", "NONEXISTENT", 5, "Great course");
        });
    }

    @Test
    void listForCourseThrowsWhenCourseNotFound() {
        given(courseService.getByCode("NONEXISTENT"))
            .willThrow(new CourseNotFoundException("NONEXISTENT"));

        assertThrows(CourseNotFoundException.class, () -> {
            reviewService.listForCourse("NONEXISTENT");
        });
    }

    @Test
    void getForCourseByLearnerThrowsWhenCourseNotFound() {
        given(courseService.getByCode("NONEXISTENT"))
            .willThrow(new CourseNotFoundException("NONEXISTENT"));

        assertThrows(CourseNotFoundException.class, () -> {
            reviewService.getForCourseByLearner("NONEXISTENT", "LRN-001");
        });
    }

    @Test
    void getForCourseByLearnerThrowsWhenLearnerNotFound() {
        Course course = new Course("CS1007", "Foundations", "Basic.", CourseLevel.FOUNDATION, 10, true);
        given(courseService.getByCode("CS1007")).willReturn(course);
        given(learnerService.getByRef("NONEXISTENT"))
            .willThrow(new LearnerNotFoundException("NONEXISTENT"));

        assertThrows(LearnerNotFoundException.class, () -> {
            reviewService.getForCourseByLearner("CS1007", "NONEXISTENT");
        });
    }
}
