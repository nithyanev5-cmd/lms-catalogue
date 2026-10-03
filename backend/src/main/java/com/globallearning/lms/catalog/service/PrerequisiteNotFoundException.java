package com.globallearning.lms.catalog.service;

public class PrerequisiteNotFoundException extends RuntimeException {

    public PrerequisiteNotFoundException(String prerequisiteCode) {
        super(String.format("Prerequisite course %s not found", prerequisiteCode));
    }
}
