package com.globallearning.lms.catalog.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.domain.EnrollmentStatus;
import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.repository.EnrollmentRepository;
import com.globallearning.lms.catalog.service.LearnerNotFoundException;
import com.globallearning.lms.catalog.service.LearnerService;
import com.globallearning.lms.catalog.web.mapper.EnrollmentMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class LearnerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LearnerService learnerService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new LearnerController(learnerService, enrollmentRepository, new EnrollmentMapper()))
            .setControllerAdvice(new EnrollmentExceptionHandler())
            .build();
    }

    @Test
    void listsLearnerEnrollments() throws Exception {
        Learner learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");
        Course course = new Course("CS1007", "Intro CS", "desc", CourseLevel.FOUNDATION, 3, true);
        when(learnerService.getByRef("L001")).thenReturn(learner);
        when(enrollmentRepository.findByLearnerOrderByCreatedAtDesc(learner))
            .thenReturn(List.of(new Enrollment(course, learner, EnrollmentStatus.ACTIVE)));

        mockMvc.perform(get("/api/learners/L001/enrollments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].courseCode").value("CS1007"))
            .andExpect(jsonPath("$[0].learnerRef").value("L001"))
            .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void returnsEmptyListWhenLearnerHasNoEnrollments() throws Exception {
        Learner learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");
        when(learnerService.getByRef("L001")).thenReturn(learner);
        when(enrollmentRepository.findByLearnerOrderByCreatedAtDesc(learner))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/learners/L001/enrollments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.empty()));
    }

    @Test
    void mapsMissingLearnerToNotFound() throws Exception {
        when(learnerService.getByRef("MISSING"))
            .thenThrow(new LearnerNotFoundException("MISSING"));

        mockMvc.perform(get("/api/learners/MISSING/enrollments"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("LEARNER_NOT_FOUND"));
    }
}