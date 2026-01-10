package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationRequestDTO;
import com.halt.medtracker.medication_tracker_api.entity.Medication;
import com.halt.medtracker.medication_tracker_api.entity.User;
import com.halt.medtracker.medication_tracker_api.repository.MedicationRepository;
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationService {
    
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    public Medication createMedication(CreateMedicationRequestDTO request){

        //current logged-in user's email from the JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

  
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                
        Medication medication = Medication.builder()
                                .user(currentUser)
                                .name(request.getName())
                                .brandName(request.getBrandName())
                                .dosage(request.getDosage()) 
                                .quantityTotal(request.getQuantity()) 
                                .quantityLeft(request.getQuantity())
                                .type(request.getType())
                                .doctorName(request.getDoctorName())
                                .instructions(request.getInstructions())
                                .imageUrl(request.getImageUrl())
                                .expiryDate(request.getExpiryDate())
                                .startDate(request.getStartDate())
                                .isActive(true) 
                                .build();
        
        return medicationRepository.save(medication);
                                
    }


}
