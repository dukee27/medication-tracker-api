package com.halt.medtracker.medication_tracker_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name="users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Handle BaseEntity fields correctly
public class User extends BaseEntity {

    // NOTE: 'id' is inherited from BaseEntity

    @Column(nullable = false, unique = true)
    private String email;

    private String passwordHash;

    private String firstName;
    private String lastName;

    private String phoneNumber;

    private LocalDate dateOfBirth;
    private String gender;

    private String timeZone;

    private String profilePictureUrl;

    private boolean isEmailVerified;
    
    // createdAt and updatedAt are inherited!
}