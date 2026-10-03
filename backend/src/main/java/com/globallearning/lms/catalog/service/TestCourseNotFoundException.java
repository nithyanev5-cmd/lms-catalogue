package com.globallearning.lms.catalog.service;

public class TestCourseNotFoundException extends RuntimeException {
    public TestCourseNotFoundException(Long id) {
        super("Test course not found with id: " + id);
    }

    public TestCourseNotFoundException(String message) {
        super(message);
    }
}
