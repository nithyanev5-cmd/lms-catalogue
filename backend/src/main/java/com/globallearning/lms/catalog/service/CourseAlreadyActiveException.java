package com.globallearning.lms.catalog.service;

public class CourseAlreadyActiveException extends RuntimeException {

    public CourseAlreadyActiveException(String code) {
        super("Course already active: " + code);
    }
}
