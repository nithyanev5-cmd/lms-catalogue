package com.globallearning.lms.catalog.service;

public class LearnerNotFoundException extends RuntimeException {
    public LearnerNotFoundException(String ref) {
        super("No learner found with ref " + ref);
    }
}
