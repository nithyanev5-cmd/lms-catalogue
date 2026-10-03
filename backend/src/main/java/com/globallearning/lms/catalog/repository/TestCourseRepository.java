package com.globallearning.lms.catalog.repository;

import com.globallearning.lms.catalog.domain.TestCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCourseRepository extends JpaRepository<TestCourse, Long> {
    List<TestCourse> findByIsActiveTrue();
    Optional<TestCourse> findByNameAndIsActiveTrue(String name);
    List<TestCourse> findByDifficultyLevel(String difficultyLevel);
}
