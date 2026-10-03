package com.globallearning.lms.catalog.repository;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    long countByCourseAndStatus(Course course, EnrollmentStatus status);
    Optional<Enrollment> findByCourseAndLearnerAndStatus(Course course, Learner learner, EnrollmentStatus status);
    List<Enrollment> findByLearnerOrderByCreatedAtDesc(Learner learner);
}
