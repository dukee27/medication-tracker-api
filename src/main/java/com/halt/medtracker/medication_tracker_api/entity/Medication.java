package com.halt.medtracker.medication_tracker_api.entity;

import com.halt.medtracker.medication_tracker_api.constants.MedicationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Medication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private String brandName;
    private String dosage;

    @Enumerated(EnumType.STRING)
    private MedicationType type;

    private int quantityTotal;
    private int quantityLeft;

    private LocalDate expiryDate; 

    private String doctorName;
    private String instructions;
    private boolean isActive;
    private String imageUrl;
}