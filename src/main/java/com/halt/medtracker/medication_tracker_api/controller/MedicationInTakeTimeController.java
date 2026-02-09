package com.halt.medtracker.medication_tracker_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationIntakeTimeRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateInTakeTimeRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationIntakeTimeResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.MedicationIntakeTimeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/intake-times")
@RequiredArgsConstructor
public class MedicationInTakeTimeController {
     private final MedicationIntakeTimeService intakeTimeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addIntakeTime(
            @RequestBody CreateMedicationIntakeTimeRequestDTO request) {

        intakeTimeService.addIntakeTime(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Intake time added", null));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationIntakeTimeResponseDTO>> updateIntakeTime(
            @PathVariable Long id,
            @RequestBody UpdateInTakeTimeRequest request) {

        MedicationIntakeTimeResponseDTO response =
                intakeTimeService.updateIntakeTime(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Intake time updated", response)
        );
    }
}
