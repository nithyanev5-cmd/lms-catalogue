package com.globallearning.lms.catalog.service;

public class CyclicPrerequisiteException extends RuntimeException {

    public CyclicPrerequisiteException(String courseCode) {
        super(String.format("Cyclic dependency detected for %s", courseCode));
    }
}
