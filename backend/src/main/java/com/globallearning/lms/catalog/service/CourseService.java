package com.globallearning.lms.catalog.service;

import com.globallearning.lms.catalog.audit.CatalogAuditLogger;
import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.repository.CourseRepository;
import java.util.Comparator;
import java.util.List;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CatalogAuditLogger auditLogger;
    private final com.globallearning.lms.catalog.repository.CoursePrerequisiteRepository prerequisiteRepository;

    public CourseService(CourseRepository courseRepository, CatalogAuditLogger auditLogger,
        com.globallearning.lms.catalog.repository.CoursePrerequisiteRepository prerequisiteRepository) {
        this.courseRepository = courseRepository;
        this.auditLogger = auditLogger;
        this.prerequisiteRepository = prerequisiteRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> listActive(CourseLevel level, String keyword) {
        List<Course> results;
        if (keyword != null && !keyword.isBlank()) {
            if (level != null) {
                results = courseRepository.searchByKeywordAndLevel(keyword.trim(), level.name());
            } else {
                results = courseRepository.searchByKeyword(keyword.trim());
            }
        } else if (level != null) {
            results = courseRepository.findByActiveTrueAndLevelAndArchivedAtNullOrderByCodeAsc(level);
            // Within a level, show the best-rated courses first.
            results = results.stream()
                .sorted(Comparator.comparing(Course::getAverageRating).reversed())
                .toList();
        } else {
            results = courseRepository.findByActiveTrueAndArchivedAtNullOrderByCodeAsc();
        }
        auditLogger.recordCatalogRead("anonymous", results.size());
        return results;
    }

    @Transactional(readOnly = true)
    public Course getByCode(String code) {
        return courseRepository.findByCodeIgnoreCase(code)
            .orElseThrow(() -> new CourseNotFoundException(code));
    }

    @Transactional(readOnly = true)
    public java.util.List<Course> getPrerequisiteCourses(String code) {
        Course course = getByCode(code);
        return prerequisiteRepository.findPrerequisiteCoursesByCourseId(course.getId());
    }

    @Transactional
    public Course addPrerequisite(String courseCode, String prerequisiteCode) {
        // Normalize handled by repository findByCodeIgnoreCase
        Course course = getByCode(courseCode);
        Course prereq = courseRepository.findByCodeIgnoreCase(prerequisiteCode)
            .orElseThrow(() -> new PrerequisiteNotFoundException(prerequisiteCode));

        if (course.getId().equals(prereq.getId())) {
            throw new CyclicPrerequisiteException(courseCode);
        }

        if (course.getArchivedAt() != null) {
            throw new CourseArchivedException(courseCode);
        }

        Integer count = prerequisiteRepository.countByCourseIdAndPrerequisiteCourseId(course.getId(), prereq.getId());
        if (count != null && count > 0) {
            throw new DuplicatePrerequisiteException(courseCode, prerequisiteCode);
        }

        prerequisiteRepository.insertPrerequisite(course.getId(), prereq.getId());
        // Optionally audit: auditLogger.recordCatalogWrite(...)
        return prereq;
    }

    @Transactional
    public void removePrerequisite(String courseCode, String prerequisiteCode) {
        Course course = getByCode(courseCode);
        Course prereq = courseRepository.findByCodeIgnoreCase(prerequisiteCode)
            .orElseThrow(() -> new PrerequisiteNotFoundException(prerequisiteCode));
        int deleted = prerequisiteRepository.deleteByCourseIdAndPrerequisiteCourseId(course.getId(), prereq.getId());
        if (deleted == 0) {
            throw new PrerequisiteNotFoundException(prerequisiteCode);
        }
    }

    @Transactional
    public Course update(String code, String title, String description, Integer credits, Integer capacity, String instructorEmail) {
        Course course = getByCode(code);
        if (course.getArchivedAt() != null) {
            throw new CourseArchivedException(code);
        }
        course.setTitle(title);
        course.setDescription(description);
        course.setCredits(credits);
        course.setCapacity(capacity);
        course.setInstructorEmail(instructorEmail);
        return courseRepository.save(course);
    }

    @Transactional
    public Course archive(String code, String reason) {
        Course course = getByCode(code);
        if (course.getArchivedAt() != null) {
            throw new CourseAlreadyArchivedException(code);
        }
        course.setArchivedAt(Instant.now());
        course.setArchiveReason(reason);
        Course saved = courseRepository.save(course);
        auditLogger.recordCourseArchived("anonymous", course.getCode(), reason);
        return saved;
    }

    // TODO(platform-team): once EnrolmentService lands, the catalogue should refuse to
    // deactivate a course that still has open enrolments or an active waitlist.
    @Transactional
    public Course deactivate(String code) {
        Course course = getByCode(code);
        course.setActive(false);
        return courseRepository.save(course);
    }

    @Transactional
    public Course reactivate(String code) {
        Course course = getByCode(code);
        if (course.isActive()) {
            throw new CourseAlreadyActiveException(code);
        }
        course.setActive(true);
        Course saved = courseRepository.save(course);
        auditLogger.recordCourseReactivated("anonymous", course.getCode());
        return saved;
    }
}
