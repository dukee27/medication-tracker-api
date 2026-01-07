package com.halt.medtracker.medication_tracker_api.entity;

import jakarta.persistence.*;
import lombok.Data; // Added Data for getters/setters in base
import java.time.LocalDateTime;

@MappedSuperclass
@Data // Generates getters/setters for id, createdAt, etc.
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isDeleted = false;

    // AUTOMATION: Automatically set timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}