package com.halt.medtracker.medication_tracker_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DoctorVisit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String doctorName;
    private String specialty;
    private String clinicName;
    private String locationCoordinates;

    private LocalDateTime visitDate;
    private LocalDate nextFollowUpDate;
    private String diagnosis;

    private Double consultationFee; 
    private String prescriptionImageUrl;

    @Lob 
    @Column(columnDefinition = "TEXT") 
    private String doctorNotes;
}