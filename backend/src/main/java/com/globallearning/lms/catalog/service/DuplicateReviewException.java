package com.globallearning.lms.catalog.service;

public class DuplicateReviewException extends RuntimeException {
    public DuplicateReviewException(String courseCode, String learnerRef) {
        super("Learner " + learnerRef + " has already reviewed course " + courseCode);
    }
}
