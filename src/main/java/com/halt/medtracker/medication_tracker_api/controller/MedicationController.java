package com.halt.medtracker.medication_tracker_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.constants.Permissions;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.mapper.MedicationMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.MedicationFilterRequest;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateMedicationRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationResponseDTO;

import com.halt.medtracker.medication_tracker_api.service.MedicationService;
import com.halt.medtracker.medication_tracker_api.service.SubjectResolver;
import com.halt.medtracker.medication_tracker_api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;
    private final SubjectResolver subjectResolver;
    private final MedicationMapper medicationMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationResponseDTO>> addMedication(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @Valid @RequestBody CreateMedicationRequestDTO request) {

        User actor = userService
                .getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_CREATE
        );

        Medication created = medicationService.createMedication(request, subject);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Medication added successfully",
                        medicationMapper.toResponse(created)
                ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationResponseDTO>> updateMedication(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateMedicationRequest request) {

        User actor = userService
                .getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_EDIT
        );

        Medication edited =
                medicationService.editMedication(id, request, subject);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Medication updated",
                        medicationMapper.toResponse(edited)
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getAllMedications(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_VIEW
        );

        List<MedicationResponseDTO> result = medicationService.getAllUserMedications(subject)
                .stream()
                .map(medicationMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.success("All medications fetched successfully", result)
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationResponseDTO>> getMedicationById(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @PathVariable Long id) {

        User actor = userService
                .getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_VIEW
        );

        Medication medication =
                medicationService.getMedicationById(id, subject);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Fetched successfully",
                        medicationMapper.toResponse(medication)
                ));
    }


    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<MedicationResponseDTO>>> searchMedications(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @RequestBody MedicationFilterRequest filter) {

        User actor = userService
                .getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_VIEW
        );

        Page<MedicationResponseDTO> result =
                medicationService
                        .filterMedication(subject, filter)
                        .map(medicationMapper::toResponse);

        return ResponseEntity.ok(ApiResponse.success("Search results", result));
    }


    @GetMapping("/reports/low-stock")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getLowStockReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_VIEW
        );

        List<MedicationResponseDTO> result =
                medicationService.getLowStockReport(subject)
                        .stream()
                        .map(medicationMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.success("Low stock report", result)
        );
    }

    @GetMapping("/reports/expiring")
    public ResponseEntity<ApiResponse<List<MedicationResponseDTO>>> getExpiryReport(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.MEDICATION_VIEW
        );

        List<MedicationResponseDTO> result =
                medicationService.getExpiryReport(subject)
                        .stream()
                        .map(medicationMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.success("Expiry report", result)
        );
    }

}
