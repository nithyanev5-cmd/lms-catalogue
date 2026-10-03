package com.globallearning.lms.catalog.web;

import com.globallearning.lms.catalog.domain.TestCourse;
import com.globallearning.lms.catalog.service.TestCourseService;
import com.globallearning.lms.catalog.web.dto.TestCourseRequest;
import com.globallearning.lms.catalog.web.dto.TestCourseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test-courses")
public class TestCourseController {
    private final TestCourseService testCourseService;

    public TestCourseController(TestCourseService testCourseService) {
        this.testCourseService = testCourseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TestCourseResponse createTestCourse(@RequestBody TestCourseRequest request) {
        TestCourse testCourse = new TestCourse();
        testCourse.setName(request.getName());
        testCourse.setDescription(request.getDescription());
        testCourse.setDurationHours(request.getDurationHours());
        testCourse.setDifficultyLevel(request.getDifficultyLevel());
        testCourse.setMaxAttempts(request.getMaxAttempts());
        testCourse.setPassingScore(request.getPassingScore());

        TestCourse created = testCourseService.createTestCourse(testCourse);
        return mapToResponse(created);
    }

    @GetMapping
    public List<TestCourseResponse> getAllTestCourses() {
        return testCourseService.getAllActiveTestCourses()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TestCourseResponse getTestCourseById(@PathVariable Long id) {
        TestCourse testCourse = testCourseService.getTestCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test course not found"));
        return mapToResponse(testCourse);
    }

    @GetMapping("/difficulty/{difficultyLevel}")
    public List<TestCourseResponse> getByDifficultyLevel(@PathVariable String difficultyLevel) {
        return testCourseService.getTestCoursesByDifficultyLevel(difficultyLevel)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public TestCourseResponse updateTestCourse(@PathVariable Long id, @RequestBody TestCourseRequest request) {
        TestCourse testCourse = new TestCourse();
        testCourse.setName(request.getName());
        testCourse.setDescription(request.getDescription());
        testCourse.setDurationHours(request.getDurationHours());
        testCourse.setDifficultyLevel(request.getDifficultyLevel());
        testCourse.setMaxAttempts(request.getMaxAttempts());
        testCourse.setPassingScore(request.getPassingScore());

        TestCourse updated = testCourseService.updateTestCourse(id, testCourse);
        return mapToResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestCourse(@PathVariable Long id) {
        testCourseService.deleteTestCourse(id);
    }

    private TestCourseResponse mapToResponse(TestCourse testCourse) {
        TestCourseResponse response = new TestCourseResponse();
        response.setId(testCourse.getId());
        response.setName(testCourse.getName());
        response.setDescription(testCourse.getDescription());
        response.setDurationHours(testCourse.getDurationHours());
        response.setDifficultyLevel(testCourse.getDifficultyLevel());
        response.setMaxAttempts(testCourse.getMaxAttempts());
        response.setPassingScore(testCourse.getPassingScore());
        response.setIsActive(testCourse.getIsActive());
        return response;
    }
}
