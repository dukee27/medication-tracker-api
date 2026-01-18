package com.halt.medtracker.medication_tracker_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.mapper.MedicationMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.MedicationFilterRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;
import com.halt.medtracker.medication_tracker_api.service.MedicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationMapper medicationMapper;
    private final UserRepository userRepository;

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
            return ResponseEntity.ok(ApiResponse.success("Fetched successfully", response));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<MedicationResponseDTO>>> searchMedications(
            @RequestBody MedicationFilterRequest filter) {
        
        Long userId = getAuthenticatedUserId();
        
        Page<MedicationResponseDTO> result = medicationService.filterMedication(userId, filter);
        
        return ResponseEntity.ok(ApiResponse.success("Search results", result));
    }

    // gotta change all this 
    @GetMapping("/reports/today")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getMedicationsForToday() {
        Long userId = getAuthenticatedUserId();
        List<MedicationResponseDTO> result = medicationService.getMedicationsForToday(userId);
        return ResponseEntity.ok(ApiResponse.success("Today's medications", result));
    }

    @GetMapping("/reports/low-stock")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getLowStockReport() {
        Long userId = getAuthenticatedUserId();
        List<MedicationResponseDTO> result = medicationService.getLowStockReport(userId);
        return ResponseEntity.ok(ApiResponse.success("Low stock report", result));
    }

    @GetMapping("/reports/expiring")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getExpiryReport() {
        Long userId = getAuthenticatedUserId();
        List<MedicationResponseDTO> result = medicationService.getExpiryReport(userId);
        return ResponseEntity.ok(ApiResponse.success("Expiry report", result));
    }
    
    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getId();
    }
}
