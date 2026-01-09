package com.halt.medtracker.medication_tracker_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateUserRequestDTO;
import com.halt.medtracker.medication_tracker_api.entity.Medication;
import com.halt.medtracker.medication_tracker_api.service.MedicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationMapper medicationMapper;

    public ResponseEntity<ApiResponse<MedicationResponseDTO>> addMedication(
        @Valid @RequestBody CreateMedicationRequestDTO request){
            Medication createMedication = medicationService.createMedication(request);
            MedicationResponseDTO medicationResponse = medicationMapper.toResponse(createdMedication);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Medication added successfully", medicationResponse));
        }
}
