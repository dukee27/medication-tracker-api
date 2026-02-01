package com.halt.medtracker.medication_tracker_api.dto.mapper;

import org.springframework.stereotype.Component;

import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationIntakeTime;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationIntakeTimeResponseDTO;

@Component
public class MedicationIntakeTimeMapper {

    public MedicationIntakeTimeResponseDTO toResponse(MedicationIntakeTime intakeTime) {
        return MedicationIntakeTimeResponseDTO.builder()
                .intakeTimeId(intakeTime.getId())
                .scheduleId(intakeTime.getSchedule().getId())
                .intakeTime(intakeTime.getIntakeTime())
                .build();
    }
}
