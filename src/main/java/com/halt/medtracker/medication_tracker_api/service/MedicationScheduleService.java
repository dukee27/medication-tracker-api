package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationScheduleService {

    private final MedicationRepository medicationRepository;
    private final MedicationScheduleRepository medicationScheduleRepository;
    private final UserRepository userRepository;
    private final MedicationScheduleMapper medicationScheduleMapper;
    @Transactional
    public MedicationScheduleResponseDTO createSchedule(
        CreateMedicationScheduleRequestDTO request ){
        User user = getCurrentUser();

        Medication medication = medicationRepository
                .findById(request.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        if (!medication.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        MedicationSchedule schedule =
            medicationScheduleMapper.toEntity(request, medication);

        MedicationSchedule saved =
            medicationScheduleRepository.save(schedule);

        return medicationScheduleMapper.toResponse(saved);
    }

    @Transactional
    public MedicationScheduleResponseDTO editSchedule(
                            Long scheduleId,
                            UpdateScheduleRequest request ){

        User user = getCurrentUser();

        MedicationSchedule schedule = medicationScheduleRepository
            .findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getMedication().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        medicationScheduleMapper.updateEntity(schedule, request);

        MedicationSchedule saved =
            medicationScheduleRepository.save(schedule);

        return medicationScheduleMapper.toResponse(saved);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}


