package com.globallearning.lms.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.globallearning.lms.catalog.domain.Course;
import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.service.CourseService;
import com.globallearning.lms.catalog.service.LegacyCourseCodeParser;
import com.globallearning.lms.catalog.web.CourseController;
import com.globallearning.lms.catalog.web.mapper.CourseMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CourseMapper.class, LegacyCourseCodeParser.class})
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Test
    void listsActiveCourses() throws Exception {
        given(courseService.listActive(nullable(CourseLevel.class), nullable(String.class)))
            .willReturn(List.of(
                new Course("CS1007", "Foundations of Programming", "Basics.", CourseLevel.FOUNDATION, 10, true),
                new Course("CS2110", "Object Oriented Design", "Design.", CourseLevel.INTERMEDIATE, 15, true)
            ));

        mockMvc.perform(get("/api/courses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].code").value("CS1007"))
            .andExpect(jsonPath("$[1].title").value("Object Oriented Design"));
    }

    @Test
    void returnsCourseDetailByCode() throws Exception {
        given(courseService.getByCode(any()))
            .willReturn(new Course("CS3401", "Distributed Systems", "Consensus.", CourseLevel.ADVANCED, 20, true));

        mockMvc.perform(get("/api/courses/CS3401"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("CS3401"))
            .andExpect(jsonPath("$.level").value("ADVANCED"));
            
        mockMvc.perform(get("/api/courses/CS3401"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.capacity").value(30));
    }

    @Test
    void reactivatesCourse() throws Exception {
        given(courseService.reactivate(any()))
            .willReturn(new Course("CS9001", "Reactivated Course", "Back.", CourseLevel.FOUNDATION, 5, true));

        mockMvc.perform(post("/api/courses/CS9001/reactivate"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("CS9001"))
            .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void reactivatingAlreadyActiveCourseIsConflict() throws Exception {
        given(courseService.reactivate(any()))
            .willThrow(new com.globallearning.lms.catalog.service.CourseAlreadyActiveException("CS9999"));

        mockMvc.perform(post("/api/courses/CS9999/reactivate"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("COURSE_ALREADY_ACTIVE"));
    }
}
