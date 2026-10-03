package com.globallearning.lms.catalog.repository;

import com.globallearning.lms.catalog.domain.Learner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerRepository extends JpaRepository<Learner, Long> {
    Optional<Learner> findByLearnerRef(String learnerRef);
}
