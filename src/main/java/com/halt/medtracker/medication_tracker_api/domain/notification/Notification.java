package com.halt.medtracker.medication_tracker_api.domain.notification;

import java.time.LocalDateTime;

import com.halt.medtracker.medication_tracker_api.constants.NotificationStatus;
import com.halt.medtracker.medication_tracker_api.constants.NotificationType;
import com.halt.medtracker.medication_tracker_api.domain.base.BaseEntity;
import com.halt.medtracker.medication_tracker_api.domain.healthcare.DoctorVisit;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.domain.medication.RefillRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime scheduledAt;

    @Column(columnDefinition = "TEXT")
    private String message;

    // Optional links
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_visit_id")
    private DoctorVisit doctorVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refill_request_id")
    private RefillRequest refillRequest;
}

