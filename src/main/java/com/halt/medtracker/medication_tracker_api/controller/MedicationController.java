package com.halt.medtracker.medication_tracker_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.mapper.MedicationMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.MedicationFilterRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.MedicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationMapper medicationMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationResponseDTO>> addMedication(
        @Valid @RequestBody CreateMedicationRequestDTO request){
            Medication createdMedication = medicationService.createMedication(request);
            MedicationResponseDTO medicationResponse = medicationMapper.toResponse(createdMedication);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Medication added successfully", medicationResponse));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationResponseDTO>> getMedicationById(@PathVariable Long id){
            Medication med = medicationService.getMedicationById(id);
            MedicationResponseDTO response = medicationMapper.toResponse(med);
            return ResponseEntity.ok(
                ApiResponse.success("Fetched successfully", response));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<MedicationResponseDTO>>> searchMedications(
            @RequestBody MedicationFilterRequest filter) {
        
        Page<MedicationResponseDTO> result = medicationService.filterMedication(filter);
        
        return ResponseEntity.ok(ApiResponse.success("Search results", result));
    }

    @GetMapping("/reports/low-stock")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getLowStockReport() {
        List<MedicationResponseDTO> result = medicationService.getLowStockReport();
        return ResponseEntity.ok(ApiResponse.success("Low stock report", result));
    }

    @GetMapping("/reports/expiring")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getExpiryReport() {
        List<MedicationResponseDTO> result = medicationService.getExpiryReport();
        return ResponseEntity.ok(ApiResponse.success("Expiry report", result));
    }
}
