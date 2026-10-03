package com.globallearning.lms.catalog.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.service.CourseService;
import com.globallearning.lms.catalog.web.dto.AddPrerequisiteRequest;
import com.globallearning.lms.catalog.web.mapper.CourseMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CoursePrerequisiteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    private CourseMapper courseMapper = new CourseMapper(new com.globallearning.lms.catalog.service.LegacyCourseCodeParser());

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        CourseController controller = new CourseController(courseService, courseMapper);
        CatalogExceptionHandler handler = new CatalogExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(handler)
            .build();
    }

    @Test
    public void getPrerequisites_emptyListReturns200() throws Exception {
        when(courseService.getPrerequisiteCourses("CS3401")).thenReturn(List.of());

        mockMvc.perform(get("/api/courses/CS3401/prerequisites"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    public void addPrerequisite_validationFails() throws Exception {
        AddPrerequisiteRequest req = new AddPrerequisiteRequest();
        req.setPrerequisiteCode("");

        mockMvc.perform(post("/api/courses/CS3401/prerequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deletePrerequisite_notFoundReturns404() throws Exception {
        org.mockito.Mockito.doThrow(new com.globallearning.lms.catalog.service.PrerequisiteNotFoundException("CS1007"))
            .when(courseService).removePrerequisite("CS3401", "CS1007");

        mockMvc.perform(delete("/api/courses/CS3401/prerequisites/CS1007"))
            .andExpect(status().isNotFound());
    }
}
