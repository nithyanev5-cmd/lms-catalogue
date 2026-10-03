package com.globallearning.lms.catalog.web.dto;

public class EnrollmentResponse {
    private String courseCode;
    private String learnerRef;
    private String status;

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getLearnerRef() { return learnerRef; }
    public void setLearnerRef(String learnerRef) { this.learnerRef = learnerRef; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
