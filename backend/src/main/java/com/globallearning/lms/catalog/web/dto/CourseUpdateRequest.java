package com.globallearning.lms.catalog.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

/**
 * Payload for editing an existing course. Validation lives here, using
 * jakarta.validation - this project is on Spring Boot 3, not Boot 2.
 */
public class CourseUpdateRequest {

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be 200 characters or fewer")
    private String title;

    @Size(max = 2000, message = "description must be 2000 characters or fewer")
    private String description;

    @NotNull(message = "credits is required")
    @Min(value = 5, message = "credits must be at least 5")
    @Max(value = 60, message = "credits must be 60 or fewer")
    private Integer credits;

    @NotNull(message = "capacity is required")
    @Min(value = 1, message = "capacity must be at least 1")
    @Max(value = 500, message = "capacity must be 500 or fewer")
    private Integer capacity;

    @Email(message = "must be a valid email")
    @Size(max = 120, message = "instructorEmail must be 120 characters or fewer")
    private String instructorEmail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }
}
