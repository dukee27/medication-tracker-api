package com.halt.medtracker.medication_tracker_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationIntakeTime;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.response.TodayMedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.repository.MedicationIntakeTimeRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationScheduleRepository;
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodayMedicationService {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private final MedicationScheduleRepository medicationScheduleRepository;
    private final MedicationIntakeTimeRepository medicationIntakeTImeRepository;

    @Transactional(readOnly = true)
    public List<TodayMedicationResponseDTO> getMedicationsForToday(){
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();

        List<MedicationSchedule> schedules = medicationScheduleRepository.findByMedication_User_Id(user.getId());

        return schedules.stream()
                .filter(schedule -> appliesToday(schedule,today))
                .flatMap(schedule->
                    medicationIntakeTImeRepository.findBySchedule_Id(schedule.getId())
                    .stream()
                    .map(time -> toResponse(schedule,time))
                )
                .sorted((a,b) -> a.getIntakeTime().compareTo(b.getIntakeTime()))
                .toList();
    }

    

    private boolean appliesToday(MedicationSchedule schedule,LocalDate today){
        return true; // have to add this yet
    }
    
    private TodayMedicationResponseDTO toResponse(
            MedicationSchedule schedule,
            MedicationIntakeTime time) {

        return TodayMedicationResponseDTO.builder()
            .medicationId(schedule.getMedication().getId())
            .medicationName(schedule.getMedication().getName())
            .dosage(schedule.getMedication().getDosage())
            .intakeTime(time.getIntakeTime())
            .intakeTiming(schedule.getIntakeTiming())
            .build();
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
