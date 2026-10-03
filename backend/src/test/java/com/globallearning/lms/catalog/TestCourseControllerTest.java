package com.globallearning.lms.catalog;

import com.globallearning.lms.catalog.domain.TestCourse;
import com.globallearning.lms.catalog.repository.TestCourseRepository;
import com.globallearning.lms.catalog.service.TestCourseService;
import com.globallearning.lms.catalog.web.TestCourseController;
import com.globallearning.lms.catalog.web.dto.TestCourseRequest;
import com.globallearning.lms.catalog.web.dto.TestCourseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestCourseRepository testCourseRepository;

    @BeforeEach
    public void setUp() {
        testCourseRepository.deleteAll();
    }

    @Test
    public void testCreateTestCourse() throws Exception {
        TestCourseRequest request = new TestCourseRequest("Java Basics", "Learn Java fundamentals", 10);
        request.setDifficultyLevel("BEGINNER");
        request.setMaxAttempts(3);
        request.setPassingScore(70);

        mockMvc.perform(post("/api/test-courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name", equalTo("Java Basics")))
                .andExpect(jsonPath("$.isActive", equalTo(true)));
    }

    @Test
    public void testGetAllTestCourses() throws Exception {
        TestCourse course1 = new TestCourse("Course 1", "Description 1", 10);
        TestCourse course2 = new TestCourse("Course 2", "Description 2", 20);
        testCourseRepository.save(course1);
        testCourseRepository.save(course2);

        mockMvc.perform(get("/api/test-courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("Course 1")))
                .andExpect(jsonPath("$[1].name", equalTo("Course 2")));
    }
}
