package com.globallearning.lms.catalog.service;

public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(String courseCode) {
        super("No seats available on course " + courseCode);
    }
}
