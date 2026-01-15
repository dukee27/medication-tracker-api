package com.halt.medtracker.medication_tracker_api.domain.access;

import com.halt.medtracker.medication_tracker_api.constants.RelationshipType;
import com.halt.medtracker.medication_tracker_api.domain.base.BaseEntity;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessControl extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id", nullable = false)
    private User caregiver;

    @Enumerated(EnumType.STRING)
    private RelationshipType relationship;

    private boolean canViewMeds;
    private boolean canEditMeds;
    private boolean canViewHistory;
    
    private boolean accessGranted;
}