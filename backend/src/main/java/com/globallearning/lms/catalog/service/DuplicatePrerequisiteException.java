package com.globallearning.lms.catalog.service;

public class DuplicatePrerequisiteException extends RuntimeException {

    public DuplicatePrerequisiteException(String courseCode, String prereqCode) {
        super(String.format("%s already requires %s", courseCode, prereqCode));
    }
}
