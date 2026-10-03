package com.globallearning.lms.catalog.service;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final LearnerService learnerService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseService courseService, LearnerService learnerService) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseService = courseService;
        this.learnerService = learnerService;
    }

    @Transactional
    public Enrollment enroll(String courseCode, String learnerRef) {
        Course course = courseService.getByCode(courseCode);
        if (!course.isActive()) {
            throw new CourseNotOpenException(courseCode);
        }
        Learner learner = learnerService.getByRef(learnerRef);

        // duplicate check
        if (enrollmentRepository.findByCourseAndLearnerAndStatus(course, learner, EnrollmentStatus.ACTIVE).isPresent()) {
            throw new DuplicateEnrollmentException(courseCode, learnerRef);
        }

        long activeSeats = enrollmentRepository.countByCourseAndStatus(course, EnrollmentStatus.ACTIVE);
        if (course.getCapacity() != null && activeSeats >= course.getCapacity()) {
            throw new SeatUnavailableException(courseCode);
        }

        Enrollment enrollment = new Enrollment(course, learner, EnrollmentStatus.ACTIVE);
        return enrollmentRepository.save(enrollment);
    }
}
