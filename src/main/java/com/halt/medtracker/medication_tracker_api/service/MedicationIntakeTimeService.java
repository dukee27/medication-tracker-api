package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.stereotype.Service;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationIntakeTime;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.mapper.MedicationIntakeTimeMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationIntakeTimeRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateInTakeTimeRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationIntakeTimeResponseDTO;
import com.halt.medtracker.medication_tracker_api.repository.MedicationIntakeTimeRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationIntakeTimeService {

    private final MedicationScheduleRepository scheduleRepository;
    private final MedicationIntakeTimeRepository intakeTimeRepository;
    private final MedicationIntakeTimeMapper intakeTimeMapper;

    @Transactional
    public MedicationIntakeTimeResponseDTO addIntakeTime(
            CreateMedicationIntakeTimeRequestDTO request,
            User subject) {

        MedicationSchedule schedule = scheduleRepository
                .findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Ownership enforcement
        if (!schedule.getMedication().getUser().getId()
                .equals(subject.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        MedicationIntakeTime intakeTime =
                intakeTimeMapper.toEntity(request, schedule);

        MedicationIntakeTime saved =
                intakeTimeRepository.save(intakeTime);

        return intakeTimeMapper.toResponse(saved);
    }

    @Transactional
    public MedicationIntakeTimeResponseDTO updateIntakeTime(
            Long intakeTimeId,
            UpdateInTakeTimeRequest request,
            User subject) {

        MedicationIntakeTime intakeTime =
                intakeTimeRepository.findById(intakeTimeId)
                        .orElseThrow(() ->
                                new RuntimeException("Intake time not found"));

        // Ownership enforcement
        if (!intakeTime.getSchedule()
                .getMedication()
                .getUser()
                .getId()
                .equals(subject.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        intakeTimeMapper.updateEntity(intakeTime, request);

        MedicationIntakeTime saved =
                intakeTimeRepository.save(intakeTime);

        return intakeTimeMapper.toResponse(saved);
    }
}
