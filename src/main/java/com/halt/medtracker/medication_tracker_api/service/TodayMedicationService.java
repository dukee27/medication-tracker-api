package com.halt.medtracker.medication_tracker_api.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationIntakeTime;
import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationSchedule;
import com.halt.medtracker.medication_tracker_api.dto.response.TodayMedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.repository.MedicationIntakeTimeRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationScheduleRepository;
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodayMedicationService {

    private final UserRepository userRepository;
    private final MedicationScheduleRepository medicationScheduleRepository;
    private final MedicationIntakeTimeRepository medicationIntakeTImeRepository;

    @Transactional(readOnly = true)
    public List<TodayMedicationResponseDTO> getMedicationsForToday(){
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();

        List<MedicationSchedule> schedules = medicationScheduleRepository.findByMedicationUserId(user.getId());

        return schedules.stream()
                .filter(schedule -> appliesToday(schedule,today))
                .flatMap(schedule->
                    medicationIntakeTImeRepository.findByScheduleId(schedule.getId())
                    .stream()
                    .map(time -> toResponse(schedule,time))
                )
                .sorted((a,b) -> a.getIntakeTime().compareTo(b.getIntakeTime()))
                .toList();
    }

    

    private boolean appliesToday(MedicationSchedule schedule,LocalDate today){
        Medication medication = schedule.getMedication();
        
        if(medication.getStartDate() != null && today.isBefore(medication.getStartDate())){
            return false;
        }
        if(medication.getStartDate() != null && today.isAfter(medication.getEndDate())){
            return false;
        }

        // check for frequency status
        switch(schedule.getFrequencyType()){
            case DAILY:
                return true;
            case WEEKLY:
                return schedule.getDayOfWeek() != null && schedule.getDayOfWeek() == today.getDayOfWeek().getValue();
            case EVERY_N_DAYS:
                if(schedule.getIntervalDays() == null)
                    return false;
                long daysBetween = 
                    ChronoUnit.DAYS.between(
                        medication.getStartDate(),
                        today
                    );
                return daysBetween % schedule.getIntervalDays() == 0;
            
            case AS_NEEDED:
                return false;
            default:
                return false;
        }   
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
