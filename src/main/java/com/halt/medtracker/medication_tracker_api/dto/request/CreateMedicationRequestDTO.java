package com.halt.medtracker.medication_tracker_api.dto.request;

import java.time.LocalDateTime;

import com.halt.medtracker.medication_tracker_api.constants.MedicationType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMedicationRequestDTO {

    @NotBlank(message = "medication name is required")
    private String name;
    private String brandName;

    @NotBlank(message = "dosage is required")
    private String dosage;

    @NotBlank(message = "quantity is required")
    private int quantity;

    private MedicationType type;
    private String doctorName;
    private String instructions;
    private String imageUrl;

    @NotBlank(message = "expiry date is required")
    private LocalDateTime expiryDate;

    @NotBlank(message = "start date is required")
    private LocalDateTime startDate;
}
