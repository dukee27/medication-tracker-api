package com.halt.medtracker.medication_tracker_api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder   
public class MedicationResponseDTO {
    private Long medicationId;
    private String medicationName;
    private LocalDateTime createdAt;
}
