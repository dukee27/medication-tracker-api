package com.halt.medtracker.medication_tracker_api.dto.mapper;

import org.springframework.stereotype.Component;

import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationScheduleResponseDTO;

@Component
public class MedicationScheduleMapper {
    public MedicationScheduleResponseDTO toResponse(MedicationSchedule schedule){
        return MedicationScheduleResponseDTO.builder()
                .scheduleId(schedule.getId())
                .medicationId(schedule.getMedication().getId())
                .frequencyType(schedule.getFrequencyType())
                .dayOfWeek(schedule.getDayOfWeek())
                .intervalDays(schedule.getIntervalDays())
                .timesPerDay(schedule.getTimesPerDay())
                .intakeTiming(schedule.getIntakeTiming())
                .build();
    }
}
