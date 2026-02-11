package com.halt.medtracker.medication_tracker_api.dto.mapper;

import org.springframework.stereotype.Component;

import com.halt.medtracker.medication_tracker_api.domain.access.AccessControl;
import com.halt.medtracker.medication_tracker_api.dto.response.AccessResponseDTO;

@Component
public class AccessControlMapper {

    public AccessResponseDTO toResponse(AccessControl access) {

        return AccessResponseDTO.builder()
                .id(access.getId())
                .patientEmail(access.getPatient().getEmail())
                .caregiverEmail(access.getCaregiver().getEmail())
                .relationship(access.getRelationship())
                .status(access.getStatus())
                .canViewMeds(access.isCanViewMeds())
                .canEditMeds(access.isCanEditMeds())
                .canViewHistory(access.isCanViewHistory())
                .build();
    }
}
