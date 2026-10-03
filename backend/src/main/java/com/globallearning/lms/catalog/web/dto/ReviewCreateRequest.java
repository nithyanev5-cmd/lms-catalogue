package com.globallearning.lms.catalog.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewCreateRequest {

    @NotBlank(message = "learnerRef must not be empty")
    private String learnerRef;

    @Min(value = 1, message = "rating must be between 1 and 5")
    @Max(value = 5, message = "rating must be between 1 and 5")
    private Integer rating;

    @Size(max = 1000, message = "comment must not exceed 1000 characters")
    private String comment;

    public String getLearnerRef() {
        return learnerRef;
    }

    public void setLearnerRef(String learnerRef) {
        this.learnerRef = learnerRef;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
