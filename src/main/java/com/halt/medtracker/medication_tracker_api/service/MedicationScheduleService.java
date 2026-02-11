package com.halt.medtracker.medication_tracker_api.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.mapper.MedicationScheduleMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationScheduleRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateScheduleRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationScheduleResponseDTO;
import com.halt.medtracker.medication_tracker_api.repository.MedicationRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationScheduleService {
    private final MedicationRepository medicationRepository;
    private final MedicationScheduleRepository medicationScheduleRepository;
    private final MedicationScheduleMapper medicationScheduleMapper;

    @Transactional
    public MedicationScheduleResponseDTO createSchedule(
            CreateMedicationScheduleRequestDTO request,
            User subject) {

        Medication medication = medicationRepository
                .findById(request.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        if (!medication.getUser().getId().equals(subject.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        MedicationSchedule schedule =
                medicationScheduleMapper.toEntity(request, medication);

        return medicationScheduleMapper.toResponse(
                medicationScheduleRepository.save(schedule)
        );
    }


    @Transactional
    public MedicationScheduleResponseDTO editSchedule(
            Long scheduleId,
            UpdateScheduleRequest request,
            User subject) {

        MedicationSchedule schedule = medicationScheduleRepository
                .findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getMedication().getUser().getId().equals(subject.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        medicationScheduleMapper.updateEntity(schedule, request);

        return medicationScheduleMapper.toResponse(
                medicationScheduleRepository.save(schedule)
        );
    }

    public List<MedicationScheduleResponseDTO> getSchedules(User subject) {

        return medicationScheduleRepository
                .findByMedicationUserId(subject.getId())
                .stream()
                .map(medicationScheduleMapper::toResponse)
                .toList();
    }
    public MedicationScheduleResponseDTO getScheduleById(
            Long scheduleId,
            User subject) {

        MedicationSchedule schedule = medicationScheduleRepository
                .findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getMedication().getUser().getId().equals(subject.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        return medicationScheduleMapper.toResponse(schedule);
    }
}


