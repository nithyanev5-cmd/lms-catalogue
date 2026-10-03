package com.globallearning.lms.catalog.service;

public class CourseAlreadyArchivedException extends RuntimeException {

    public CourseAlreadyArchivedException(String code) {
        super("Course already archived: " + code);
    }
}
