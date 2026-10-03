package com.globallearning.lms.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.service.LegacyCourseCodeParser;
import com.globallearning.lms.catalog.web.dto.CourseResponse;
import com.globallearning.lms.catalog.web.dto.CourseSummaryResponse;
import org.junit.jupiter.api.Test;

class CourseMapperTest {

    private final com.globallearning.lms.catalog.web.mapper.CourseMapper mapper =
        new com.globallearning.lms.catalog.web.mapper.CourseMapper(new LegacyCourseCodeParser());

    @Test
    void mapsCourseToDetailResponse() {
        Course course = new Course("CS2110", "Object Oriented Design", "Design principles.",
            CourseLevel.INTERMEDIATE, 15, true);

        CourseResponse response = mapper.toResponse(course);

        assertThat(response.getCode()).isEqualTo("CS2110");
        assertThat(response.getTitle()).isEqualTo("Object Oriented Design");
        assertThat(response.getLevel()).isEqualTo("INTERMEDIATE");
        assertThat(response.getFaculty()).isEqualTo("CS");
        assertThat(response.getCapacity()).isEqualTo(30);
        assertThat(response.getInstructorEmail()).isNull();
    }

    @Test
    void mapsCourseToSummary() {
        Course course = new Course("DATA1105", "Introduction to Data Literacy", "Data basics.",
            CourseLevel.FOUNDATION, 10, true);

        CourseSummaryResponse summary = mapper.toSummary(course);

        assertThat(summary.getCode()).isEqualTo("DATA1105");
        assertThat(summary.getCredits()).isEqualTo(10);
    }
}
