package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationScheduleRequest;
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

    @Transactional
    public MedicationScheduleResponseDTO createSchedule(
        CreateMedicationScheduleRequest request ){
             User user = getCurrentUser();

        Medication medication = medicationRepository
                .findById(request.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        if (!medication.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        MedicationSchedule schedule = MedicationSchedule.builder()
                .medication(medication)
                .frequencyType(request.getFrequencyType())
                .dayOfWeek(request.getDayOfWeek())
                .intervalDays(request.getIntervalDays())
                .timesPerDay(request.getTimesPerDay())
                .intakeTiming(request.getIntakeTiming())
                .build();

        MedicationSchedule saved = medicationScheduleRepository.save(schedule);

        return MedicationScheduleResponseDTO.builder()
                .scheduleId(saved.getId())
                .medicationId(medication.getId())
                .frequencyType(saved.getFrequencyType())
                .dayOfWeek(saved.getDayOfWeek())
                .intervalDays(saved.getIntervalDays())
                .timesPerDay(saved.getTimesPerDay())
                .intakeTiming(saved.getIntakeTiming())
                .build();
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


