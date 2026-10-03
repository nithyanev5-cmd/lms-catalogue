package com.globallearning.lms.catalog.service;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String code) {
        super("No course found with code " + code);
    }
}
