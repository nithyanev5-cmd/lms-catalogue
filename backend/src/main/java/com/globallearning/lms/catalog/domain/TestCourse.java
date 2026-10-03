package com.globallearning.lms.catalog.domain;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "test_course")
public class TestCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "duration_hours")
    private Integer durationHours;

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Column(name = "passing_score")
    private Integer passingScore;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public TestCourse() {}

    public TestCourse(String name, String description, Integer durationHours) {
        this.name = name;
        this.description = description;
        this.durationHours = durationHours;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCourse that = (TestCourse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestCourse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durationHours=" + durationHours +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
