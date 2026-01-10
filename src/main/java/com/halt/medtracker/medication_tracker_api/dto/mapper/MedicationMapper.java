package com.halt.medtracker.medication_tracker_api.dto.mapper;

import org.springframework.stereotype.Component;

import com.halt.medtracker.medication_tracker_api.dto.response.MedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.entity.Medication;

@Component
public class MedicationMapper {
    public MedicationResponseDTO toResponse(Medication medication){
        return MedicationResponseDTO.builder()
                .id(medication.getId())
                .name(medication.getName())
                .brandName(medication.getDosage())
                .type(medication.getType())
                .quantityLeft(medication.getQuantityLeft())
                .isActive(medication.isActive())
                .expiryDate(medication.getExpiryDate())
                .startDate(medication.getStartDate())
                .instructions(medication.getInstructions())
                .doctorName(medication.getDoctorName())
                .imageUrl(medication.getImageUrl())
                .build();
    }
}
