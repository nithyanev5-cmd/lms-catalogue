package com.globallearning.lms.catalog.web;

import com.globallearning.lms.catalog.service.EnrollmentService;
import com.globallearning.lms.catalog.web.dto.EnrollmentCreateRequest;
import com.globallearning.lms.catalog.web.dto.EnrollmentResponse;
import com.globallearning.lms.catalog.web.mapper.EnrollmentMapper;
import com.globallearning.lms.catalog.domain.Enrollment;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentController(EnrollmentService enrollmentService, EnrollmentMapper enrollmentMapper) {
        this.enrollmentService = enrollmentService;
        this.enrollmentMapper = enrollmentMapper;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody EnrollmentCreateRequest request) {
        Enrollment enrollment = enrollmentService.enroll(request.getCourseCode(), request.getLearnerRef());
        EnrollmentResponse resp = enrollmentMapper.toResponse(enrollment);
        return ResponseEntity.status(201).body(resp);
    }
}
