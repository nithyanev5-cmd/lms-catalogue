package com.globallearning.lms.catalog.service;

public class NotEnrolledException extends RuntimeException {
    public NotEnrolledException(String courseCode, String learnerRef) {
        super("Learner " + learnerRef + " is not enrolled in course " + courseCode);
    }
}
