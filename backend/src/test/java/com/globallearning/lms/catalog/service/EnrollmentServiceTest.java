package com.globallearning.lms.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.repository.EnrollmentRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private LearnerService learnerService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Course course;
    private Learner learner;

    @BeforeEach
    void setUp() {
        course = new Course("CS1007", "Intro CS", "desc", CourseLevel.FOUNDATION, 3, true);
        learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");
        lenient().when(courseService.getByCode("CS1007")).thenReturn(course);
        lenient().when(learnerService.getByRef("L001")).thenReturn(learner);
        lenient().when(enrollmentRepository.findByCourseAndLearnerAndStatus(
            course, learner, EnrollmentStatus.ACTIVE)).thenReturn(Optional.empty());
    }

    @Test
    void enrollsActiveLearnerWhenCapacityIsAvailable() {
        when(enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.ACTIVE))
            .thenReturn(0L);
        when(enrollmentRepository.save(any(Enrollment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.enroll("CS1007", "L001");

        assertEquals(course, result.getCourse());
        assertEquals(learner, result.getLearner());
        assertEquals(EnrollmentStatus.ACTIVE, result.getStatus());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void allowsEnrollmentWhenCapacityIsNull() {
        course.setCapacity(null);
        when(enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.ACTIVE))
            .thenReturn(100L);
        when(enrollmentRepository.save(any(Enrollment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.enroll("CS1007", "L001");

        assertEquals(EnrollmentStatus.ACTIVE, result.getStatus());
    }

    @Test
    void rejectsEnrollmentForInactiveCourse() {
        course.setActive(false);

        assertThrows(CourseNotOpenException.class,
            () -> enrollmentService.enroll("CS1007", "L001"));
    }

    @Test
    void rejectsDuplicateActiveEnrollment() {
        when(enrollmentRepository.findByCourseAndLearnerAndStatus(
            course, learner, EnrollmentStatus.ACTIVE))
            .thenReturn(Optional.of(new Enrollment(course, learner, EnrollmentStatus.ACTIVE)));

        assertThrows(DuplicateEnrollmentException.class,
            () -> enrollmentService.enroll("CS1007", "L001"));
    }

    @Test
    void rejectsEnrollmentWhenCapacityIsReached() {
        course.setCapacity(2);
        when(enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.ACTIVE))
            .thenReturn(2L);

        assertThrows(SeatUnavailableException.class,
            () -> enrollmentService.enroll("CS1007", "L001"));
    }

    @Test
    void propagatesMissingCourse() {
        when(courseService.getByCode("MISSING"))
            .thenThrow(new CourseNotFoundException("MISSING"));

        assertThrows(CourseNotFoundException.class,
            () -> enrollmentService.enroll("MISSING", "L001"));
    }

    @Test
    void propagatesMissingLearner() {
        when(learnerService.getByRef("MISSING"))
            .thenThrow(new LearnerNotFoundException("MISSING"));

        assertThrows(LearnerNotFoundException.class,
            () -> enrollmentService.enroll("CS1007", "MISSING"));
    }
}