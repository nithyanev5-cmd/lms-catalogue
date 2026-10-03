package com.globallearning.lms.catalog.web;

import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.service.EnrollmentService;
import com.globallearning.lms.catalog.service.LearnerService;
import com.globallearning.lms.catalog.web.dto.EnrollmentResponse;
import com.globallearning.lms.catalog.web.mapper.EnrollmentMapper;
import com.globallearning.lms.catalog.repository.EnrollmentRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learners")
public class LearnerController {

    private final LearnerService learnerService;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    public LearnerController(LearnerService learnerService, EnrollmentRepository enrollmentRepository, EnrollmentMapper enrollmentMapper) {
        this.learnerService = learnerService;
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @GetMapping("/{learnerRef}/enrollments")
    public List<EnrollmentResponse> listEnrollments(@PathVariable("learnerRef") String learnerRef) {
        var learner = learnerService.getByRef(learnerRef);
        return enrollmentRepository.findByLearnerOrderByCreatedAtDesc(learner).stream()
            .map(enrollmentMapper::toResponse)
            .toList();
    }
}
