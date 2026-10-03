package com.globallearning.lms.catalog.service;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String courseCode, String learnerRef) {
        super("No review found for learner " + learnerRef + " in course " + courseCode);
    }
}
