package com.globallearning.lms.catalog.repository;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.domain.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Review.course and Review.learner are both LAZY, and spring.jpa.open-in-view
     * is false, so the persistence context is closed by the time ReviewMapper
     * reads getLearner().getLearnerRef() in the web layer. Fetch both associations
     * with the query rather than leaving the mapper to trip over a dead proxy.
     */
    @EntityGraph(attributePaths = {"course", "learner"})
    Optional<Review> findByCourseAndLearner(Course course, Learner learner);

    @EntityGraph(attributePaths = {"course", "learner"})
    List<Review> findByCourseOrderByCreatedAtDesc(Course course);
}
