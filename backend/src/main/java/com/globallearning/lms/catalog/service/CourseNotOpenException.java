package com.globallearning.lms.catalog.service;

public class CourseNotOpenException extends RuntimeException {
    public CourseNotOpenException(String code) {
        super("Course not open: " + code);
    }
}
