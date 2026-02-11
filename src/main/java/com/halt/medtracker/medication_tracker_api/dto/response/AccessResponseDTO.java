package com.halt.medtracker.medication_tracker_api.dto.response;

import com.halt.medtracker.medication_tracker_api.constants.AccessStatus;
import com.halt.medtracker.medication_tracker_api.constants.RelationshipType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessResponseDTO {

    private Long id;

    private String patientEmail;
    private String caregiverEmail;

    private RelationshipType relationship;
    private AccessStatus status;

    private boolean canViewMeds;
    private boolean canEditMeds;
    private boolean canViewHistory;
}
