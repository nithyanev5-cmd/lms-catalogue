package com.globallearning.lms.catalog.web.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.domain.Review;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ReviewMapperTest {

    private final ReviewMapper mapper = new ReviewMapper();

    @Test
    void mapsReviewFieldsAndNullableComment() {
        Course course = new Course("CS1007", "Intro CS", "desc", CourseLevel.FOUNDATION, 3, true);
        Learner learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");
        Review review = new Review(course, learner, 5, null);
        Instant createdAt = review.getCreatedAt();

        var response = mapper.toResponse(review);

        assertEquals("CS1007", response.getCourseCode());
        assertEquals("L001", response.getLearnerRef());
        assertEquals(5, response.getRating());
        assertNull(response.getComment());
        assertEquals(createdAt, response.getCreatedAt());
        assertNull(response.getUpdatedAt());
    }
}