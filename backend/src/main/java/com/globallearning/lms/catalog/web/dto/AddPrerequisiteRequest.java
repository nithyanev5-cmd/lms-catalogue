package com.globallearning.lms.catalog.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddPrerequisiteRequest {

    @NotBlank(message = "prerequisiteCode is required")
    @Size(max = 32, message = "prerequisiteCode must be at most 32 characters")
    private String prerequisiteCode;

    public String getPrerequisiteCode() {
        return prerequisiteCode;
    }

    public void setPrerequisiteCode(String prerequisiteCode) {
        this.prerequisiteCode = prerequisiteCode;
    }
}
