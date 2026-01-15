package com.halt.medtracker.medication_tracker_api.domain.medication;

import com.halt.medtracker.medication_tracker_api.constants.FrequencyType;
import com.halt.medtracker.medication_tracker_api.domain.base.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicationSchedule extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Enumerated(EnumType.STRING)
    private FrequencyType frequency;

    private LocalTime scheduleTime; 
    private Integer dayOfWeek;
}