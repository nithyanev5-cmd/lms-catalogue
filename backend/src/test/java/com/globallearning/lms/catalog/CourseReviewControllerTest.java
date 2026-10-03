package com.globallearning.lms.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.domain.Review;
import com.globallearning.lms.catalog.service.ReviewService;
import com.globallearning.lms.catalog.web.CourseReviewController;
import com.globallearning.lms.catalog.web.mapper.ReviewMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ReviewMapper.class)
class CourseReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createsReviewSuccessfully() throws Exception {
        Course course = new Course("CS1007", "Foundations", "Basic.", CourseLevel.FOUNDATION, 10, true);
        Learner learner = new Learner("LRN-001", "John", "Doe", "john@example.com");
        Review review = new Review(course, learner, 5, "Great course");
        
        given(reviewService.create(anyString(), anyString(), any(Integer.class), anyString()))
            .willReturn(review);

        mockMvc.perform(post("/api/courses/CS1007/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new java.util.HashMap<String, Object>() {{
                put("learnerRef", "LRN-001");
                put("rating", 5);
                put("comment", "Great course");
            }})))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.courseCode").value("CS1007"))
            .andExpect(jsonPath("$.learnerRef").value("LRN-001"))
            .andExpect(jsonPath("$.rating").value(5))
            .andExpect(jsonPath("$.comment").value("Great course"));
    }

    @Test
    void listReviewsForCourse() throws Exception {
        Course course = new Course("CS1007", "Foundations", "Basic.", CourseLevel.FOUNDATION, 10, true);
        Learner learner = new Learner("LRN-001", "John", "Doe", "john@example.com");
        Review review = new Review(course, learner, 5, "Great course");

        given(reviewService.listForCourse("CS1007"))
            .willReturn(List.of(review));

        mockMvc.perform(get("/api/courses/CS1007/reviews"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].learnerRef").value("LRN-001"));
    }

    @Test
    void getsReviewByLearner() throws Exception {
        Course course = new Course("CS1007", "Foundations", "Basic.", CourseLevel.FOUNDATION, 10, true);
        Learner learner = new Learner("LRN-001", "John", "Doe", "john@example.com");
        Review review = new Review(course, learner, 5, "Great course");

        given(reviewService.getForCourseByLearner("CS1007", "LRN-001"))
            .willReturn(review);

        mockMvc.perform(get("/api/courses/CS1007/reviews/LRN-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.learnerRef").value("LRN-001"))
            .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void updatesReviewSuccessfully() throws Exception {
        Course course = new Course("CS1007", "Foundations", "Basic.", CourseLevel.FOUNDATION, 10, true);
        Learner learner = new Learner("LRN-001", "John", "Doe", "john@example.com");
        Review review = new Review(course, learner, 4, "Good course");

        given(reviewService.update(anyString(), anyString(), any(Integer.class), anyString()))
            .willReturn(review);

        mockMvc.perform(put("/api/courses/CS1007/reviews/LRN-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new java.util.HashMap<String, Object>() {{
                put("rating", 4);
                put("comment", "Good course");
            }})))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rating").value(4))
            .andExpect(jsonPath("$.comment").value("Good course"));
    }

    @Test
    void deletesReviewSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/courses/CS1007/reviews/LRN-001"))
            .andExpect(status().isNoContent());
    }

    @Test
    void returnsValidationErrorForInvalidRating() throws Exception {
        mockMvc.perform(post("/api/courses/CS1007/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new java.util.HashMap<String, Object>() {{
                put("learnerRef", "LRN-001");
                put("rating", 10);
                put("comment", "Great course");
            }})))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"))
            .andExpect(jsonPath("$.fields.rating").exists());
    }
}
