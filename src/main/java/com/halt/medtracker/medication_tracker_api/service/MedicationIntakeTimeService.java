package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.stereotype.Service;

import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationIntakeTime;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationIntakeTimeRequestDTO;
import com.halt.medtracker.medication_tracker_api.repository.MedicationIntakeTimeRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationIntakeTimeService {
    private final MedicationScheduleRepository scheduleRepository;
    private final MedicationIntakeTimeRepository intakeTimeRepository;

    @Transactional
    public void addIntakeTime(CreateMedicationIntakeTimeRequestDTO request) {

        MedicationSchedule schedule = scheduleRepository
                .findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        MedicationIntakeTime intakeTime = MedicationIntakeTime.builder()
                .schedule(schedule)
                .intakeTime(request.getIntakeTime())
                .build();

        intakeTimeRepository.save(intakeTime);
    }
}
