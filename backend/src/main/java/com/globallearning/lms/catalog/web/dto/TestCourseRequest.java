package com.globallearning.lms.catalog.web.dto;

public class TestCourseRequest {
    private String name;
    private String description;
    private Integer durationHours;
    private String difficultyLevel;
    private Integer maxAttempts;
    private Integer passingScore;

    public TestCourseRequest() {}

    public TestCourseRequest(String name, String description, Integer durationHours) {
        this.name = name;
        this.description = description;
        this.durationHours = durationHours;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }
}
