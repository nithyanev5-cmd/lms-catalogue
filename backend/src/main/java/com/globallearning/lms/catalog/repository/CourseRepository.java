package com.globallearning.lms.catalog.repository;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByActiveTrueOrderByCodeAsc();

    List<Course> findByActiveTrueAndLevelOrderByCodeAsc(CourseLevel level);

    List<Course> findByActiveTrueAndArchivedAtNullOrderByCodeAsc();

    List<Course> findByActiveTrueAndLevelAndArchivedAtNullOrderByCodeAsc(CourseLevel level);

    Optional<Course> findByCodeIgnoreCase(String code);

    /**
     * Keyword search across code and title.
     *
     * Written as a native query because the LIKE/LOWER combination performs better
     * against the ix_course_title index than the JPQL equivalent on MySQL 8.
     */
    @Query(
          value = "SELECT id, code, title, description, level, credits, active, capacity, "
              + "       instructor_email, created_at, archived_at, archive_reason "
              + "FROM course "
              + "WHERE active = 1 AND archived_at IS NULL "
              + "  AND (LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
              + "   OR LOWER(code) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
              + "ORDER BY code",
        nativeQuery = true
    )
    List<Course> searchByKeyword(@Param("keyword") String keyword);

    @Query(
          value = "SELECT id, code, title, description, level, credits, active, capacity, "
              + "       instructor_email, created_at, archived_at, archive_reason "
              + "FROM course "
              + "WHERE active = 1 AND archived_at IS NULL "
              + "  AND level = :level "
              + "  AND (LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
              + "   OR LOWER(code) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
              + "ORDER BY code",
        nativeQuery = true
    )
    List<Course> searchByKeywordAndLevel(@Param("keyword") String keyword, @Param("level") String level);
}
