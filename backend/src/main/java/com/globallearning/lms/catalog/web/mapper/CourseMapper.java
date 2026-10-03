package com.globallearning.lms.catalog.web.mapper;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.service.LegacyCourseCodeParser;
import com.globallearning.lms.catalog.web.dto.CourseResponse;
import com.globallearning.lms.catalog.web.dto.CourseSummaryResponse;
import org.springframework.stereotype.Component;

/**
 * Hand-written mapper. The team decided against MapStruct on this slice so that
 * the projection into API shapes stays explicit and reviewable.
 */
@Component
public class CourseMapper {

    private final LegacyCourseCodeParser codeParser;

    public CourseMapper(LegacyCourseCodeParser codeParser) {
        this.codeParser = codeParser;
    }

    public CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCode(course.getCode());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setLevel(course.getLevel().name());
        response.setCredits(course.getCredits());
        response.setActive(course.isActive());
        response.setFaculty(codeParser.facultyOf(course.getCode()));
        response.setInstructorEmail(course.getInstructorEmail());
        response.setCapacity(course.getCapacity());
        response.setArchivedAt(course.getArchivedAt());
        response.setArchiveReason(course.getArchiveReason());
        response.setAverageRating(course.getAverageRating());
        response.setReviewCount(course.getReviewCount());
        return response;
    }

    public CourseSummaryResponse toSummary(Course course) {
        CourseSummaryResponse summary = new CourseSummaryResponse();
        summary.setId(course.getId());
        summary.setCode(course.getCode());
        summary.setTitle(course.getTitle());
        summary.setLevel(course.getLevel().name());
        summary.setCredits(course.getCredits());
        return summary;
    }
}
