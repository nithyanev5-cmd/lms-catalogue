package com.globallearning.lms.catalog.service;

public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(String courseCode, String learnerRef) {
        super("Learner " + learnerRef + " already enrolled on course " + courseCode);
    }
}
