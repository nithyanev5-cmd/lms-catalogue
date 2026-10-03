package com.globallearning.lms.catalog.web.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import org.junit.jupiter.api.Test;

class EnrollmentMapperTest {

    private final EnrollmentMapper mapper = new EnrollmentMapper();

    @Test
    void mapsEnrollmentFields() {
        Course course = new Course("CS1007", "Intro CS", "desc", CourseLevel.FOUNDATION, 3, true);
        Learner learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");

        var response = mapper.toResponse(new Enrollment(course, learner, EnrollmentStatus.ACTIVE));

        assertEquals("CS1007", response.getCourseCode());
        assertEquals("L001", response.getLearnerRef());
        assertEquals("ACTIVE", response.getStatus());
    }
}