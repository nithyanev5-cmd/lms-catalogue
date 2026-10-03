package com.globallearning.lms.catalog.web;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.service.CourseNotOpenException;
import com.globallearning.lms.catalog.service.DuplicateEnrollmentException;
import com.globallearning.lms.catalog.service.EnrollmentService;
import com.globallearning.lms.catalog.service.LearnerNotFoundException;
import com.globallearning.lms.catalog.service.SeatUnavailableException;
import com.globallearning.lms.catalog.web.mapper.EnrollmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EnrollmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new EnrollmentController(enrollmentService, new EnrollmentMapper()))
            .setControllerAdvice(new EnrollmentExceptionHandler())
            .build();
    }

    @Test
    void createsEnrollment() throws Exception {
        when(enrollmentService.enroll("CS1007", "L001")).thenReturn(enrollment());

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseCode\":\"CS1007\",\"learnerRef\":\"L001\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.courseCode").value("CS1007"))
            .andExpect(jsonPath("$.learnerRef").value("L001"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void mapsDuplicateEnrollmentToConflict() throws Exception {
        doThrow(new DuplicateEnrollmentException("CS1007", "L001"))
            .when(enrollmentService).enroll("CS1007", "L001");

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseCode\":\"CS1007\",\"learnerRef\":\"L001\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("DUPLICATE_ENROLLMENT"));
    }

    @Test
    void mapsSeatUnavailableToConflict() throws Exception {
        doThrow(new SeatUnavailableException("CS1007"))
            .when(enrollmentService).enroll("CS1007", "L001");

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseCode\":\"CS1007\",\"learnerRef\":\"L001\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("SEAT_UNAVAILABLE"));
    }

    @Test
    void mapsCourseNotOpenToConflict() throws Exception {
        doThrow(new CourseNotOpenException("CS1007"))
            .when(enrollmentService).enroll("CS1007", "L001");

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseCode\":\"CS1007\",\"learnerRef\":\"L001\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("COURSE_NOT_OPEN"));
    }

    @Test
    void mapsMissingLearnerToNotFound() throws Exception {
        doThrow(new LearnerNotFoundException("L001"))
            .when(enrollmentService).enroll("CS1007", "L001");

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseCode\":\"CS1007\",\"learnerRef\":\"L001\"}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("LEARNER_NOT_FOUND"));
    }

    private Enrollment enrollment() {
        Course course = new Course("CS1007", "Intro CS", "desc", CourseLevel.FOUNDATION, 3, true);
        Learner learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");
        return new Enrollment(course, learner, EnrollmentStatus.ACTIVE);
    }
}