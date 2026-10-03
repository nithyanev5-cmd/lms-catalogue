package com.globallearning.lms.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "learner")
public class Learner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "learner_ref", nullable = false, unique = true, length = 32)
    private String learnerRef;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Learner() {
        // for JPA
    }

    public Learner(String learnerRef, String firstName, String lastName, String email) {
        this.learnerRef = learnerRef;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getLearnerRef() {
        return learnerRef;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
