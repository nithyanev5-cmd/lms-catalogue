package com.globallearning.lms.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_id", nullable = false)
    private Learner learner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Enrollment() {
        // for JPA
    }

    public Enrollment(Course course, Learner learner, EnrollmentStatus status) {
        this.course = course;
        this.learner = learner;
        this.status = status;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Course getCourse() { return course; }
    public Learner getLearner() { return learner; }
    public EnrollmentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
}
