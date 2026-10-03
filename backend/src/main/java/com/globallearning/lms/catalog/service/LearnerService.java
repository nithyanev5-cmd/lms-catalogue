package com.globallearning.lms.catalog.service;

import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.repository.LearnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LearnerService {

    private final LearnerRepository learnerRepository;

    public LearnerService(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    @Transactional(readOnly = true)
    public Learner getByRef(String learnerRef) {
        return learnerRepository.findByLearnerRef(learnerRef)
            .orElseThrow(() -> new LearnerNotFoundException(learnerRef));
    }
}
