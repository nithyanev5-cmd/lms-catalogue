package com.globallearning.lms.catalog.service;

public class CourseArchivedException extends RuntimeException {

    public CourseArchivedException(String code) {
        super("Course is archived: " + code);
    }
}
