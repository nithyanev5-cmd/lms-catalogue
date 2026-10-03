package com.globallearning.lms.catalog.web.dto;

public class EnrollmentCreateRequest {
    private String courseCode;
    private String learnerRef;

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getLearnerRef() { return learnerRef; }
    public void setLearnerRef(String learnerRef) { this.learnerRef = learnerRef; }
}
