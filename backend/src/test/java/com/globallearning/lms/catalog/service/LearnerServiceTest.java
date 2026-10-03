package com.globallearning.lms.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.globallearning.lms.catalog.domain.Learner;
import com.globallearning.lms.catalog.repository.LearnerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LearnerServiceTest {

    @Mock
    private LearnerRepository learnerRepository;

    @InjectMocks
    private LearnerService learnerService;

    @Test
    void returnsLearnerByReference() {
        Learner learner = new Learner("L001", "Ada", "Lovelace", "ada@example.com");
        when(learnerRepository.findByLearnerRef("L001")).thenReturn(Optional.of(learner));

        assertEquals(learner, learnerService.getByRef("L001"));
    }

    @Test
    void throwsWhenReferenceDoesNotExist() {
        when(learnerRepository.findByLearnerRef("MISSING")).thenReturn(Optional.empty());

        assertThrows(LearnerNotFoundException.class,
            () -> learnerService.getByRef("MISSING"));
    }
}