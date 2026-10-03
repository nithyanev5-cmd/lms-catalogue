package com.globallearning.lms.catalog.web;

import com.globallearning.lms.catalog.domain.CourseLevel;
import com.globallearning.lms.catalog.service.CourseService;
import com.globallearning.lms.catalog.web.dto.AddPrerequisiteRequest;
import com.globallearning.lms.catalog.web.dto.CourseResponse;
import com.globallearning.lms.catalog.web.dto.CourseSummaryResponse;
import com.globallearning.lms.catalog.web.dto.CourseUpdateRequest;
import com.globallearning.lms.catalog.web.mapper.CourseMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.globallearning.lms.catalog.web.dto.ArchiveRequest;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public List<CourseSummaryResponse> list(
        @RequestParam(name = "level", required = false) CourseLevel level,
        @RequestParam(name = "q", required = false) String keyword
    ) {
        return courseService.listActive(level, keyword).stream()
            .map(courseMapper::toSummary)
            .toList();
    }

    @GetMapping("/{code}")
    public CourseResponse getByCode(@PathVariable("code") String code) {
        return courseMapper.toResponse(courseService.getByCode(code));
    }

    @GetMapping("/{code}/prerequisites")
    public List<CourseResponse> getPrerequisites(@PathVariable("code") String code) {
        return courseService.getPrerequisiteCourses(code).stream()
            .map(courseMapper::toResponse)
            .toList();
    }

    @PutMapping("/{code}")
    public CourseResponse update(
        @PathVariable("code") String code,
        @Valid @RequestBody CourseUpdateRequest request
    ) {
        return courseMapper.toResponse(courseService.update(
            code, request.getTitle(), request.getDescription(), request.getCredits(), request.getCapacity(), request.getInstructorEmail()));
    }

    @PostMapping("/{code}/deactivate")
    public ResponseEntity<CourseResponse> deactivate(@PathVariable("code") String code) {
        return ResponseEntity.ok(courseMapper.toResponse(courseService.deactivate(code)));
    }

    @PostMapping("/{code}/reactivate")
    public ResponseEntity<CourseResponse> reactivate(@PathVariable("code") String code) {
        return ResponseEntity.ok(courseMapper.toResponse(courseService.reactivate(code)));
    }

    @PostMapping("/{code}/archive")
    public ResponseEntity<CourseResponse> archive(
        @PathVariable("code") String code,
        @Valid @RequestBody ArchiveRequest request
    ) {
        return ResponseEntity.ok(courseMapper.toResponse(courseService.archive(code, request.getReason())));
    }

    @PostMapping("/{code}/prerequisites")
    public ResponseEntity<CourseResponse> addPrerequisite(
        @PathVariable("code") String code,
        @Valid @RequestBody AddPrerequisiteRequest request
    ) {
        CourseResponse resp = courseMapper.toResponse(courseService.addPrerequisite(code, request.getPrerequisiteCode()));
        return ResponseEntity.status(201).body(resp);
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{code}/prerequisites/{prerequisiteCode}")
    public ResponseEntity<Void> deletePrerequisite(
        @PathVariable("code") String code,
        @PathVariable("prerequisiteCode") String prerequisiteCode
    ) {
        courseService.removePrerequisite(code, prerequisiteCode);
        return ResponseEntity.noContent().build();
    }
}
