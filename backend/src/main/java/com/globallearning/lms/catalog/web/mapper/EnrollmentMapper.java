package com.globallearning.lms.catalog.web.mapper;

import com.globallearning.lms.catalog.domain.Enrollment;
import com.globallearning.lms.catalog.web.dto.EnrollmentResponse;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    public EnrollmentResponse toResponse(Enrollment e) {
        EnrollmentResponse r = new EnrollmentResponse();
        r.setCourseCode(e.getCourse().getCode());
        r.setLearnerRef(e.getLearner().getLearnerRef());
        r.setStatus(e.getStatus().name());
        return r;
    }
}
