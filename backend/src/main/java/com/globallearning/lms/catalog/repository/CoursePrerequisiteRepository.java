package com.globallearning.lms.catalog.repository;

import com.globallearning.lms.catalog.domain.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePrerequisiteRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT c.* FROM course c JOIN course_prerequisite cp ON cp.prerequisite_course_id = c.id WHERE cp.course_id = :courseId ORDER BY c.code", nativeQuery = true)
    List<Course> findPrerequisiteCoursesByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT COUNT(1) FROM course_prerequisite WHERE course_id = :courseId AND prerequisite_course_id = :prereqId", nativeQuery = true)
    Integer countByCourseIdAndPrerequisiteCourseId(@Param("courseId") Long courseId, @Param("prereqId") Long prereqId);

    @Modifying
    @Query(value = "INSERT INTO course_prerequisite (course_id, prerequisite_course_id) VALUES (:courseId, :prereqId)", nativeQuery = true)
    int insertPrerequisite(@Param("courseId") Long courseId, @Param("prereqId") Long prereqId);

    @Modifying
    @Query(value = "DELETE FROM course_prerequisite WHERE course_id = :courseId AND prerequisite_course_id = :prereqId", nativeQuery = true)
    int deleteByCourseIdAndPrerequisiteCourseId(@Param("courseId") Long courseId, @Param("prereqId") Long prereqId);
}
