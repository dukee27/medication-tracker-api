package com.halt.medtracker.medication_tracker_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.halt.medtracker.medication_tracker_api.constants.Permissions;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationIntakeTimeRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateInTakeTimeRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationIntakeTimeResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.MedicationIntakeTimeService;
import com.halt.medtracker.medication_tracker_api.service.SubjectResolver;
import com.halt.medtracker.medication_tracker_api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/intake-times")
@RequiredArgsConstructor
public class MedicationInTakeTimeController {

    private final MedicationIntakeTimeService intakeTimeService;
    private final UserService userService;
    private final SubjectResolver subjectResolver;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationIntakeTimeResponseDTO>> addIntakeTime(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @RequestBody CreateMedicationIntakeTimeRequestDTO request) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.INTAKE_TIME_CREATE
        );

        MedicationIntakeTimeResponseDTO response =
                intakeTimeService.addIntakeTime(request, subject);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Intake time added", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationIntakeTimeResponseDTO>> updateIntakeTime(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @PathVariable Long id,
            @RequestBody UpdateInTakeTimeRequest request) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.INTAKE_TIME_EDIT
        );

        MedicationIntakeTimeResponseDTO response =
                intakeTimeService.updateIntakeTime(id, request, subject);

        return ResponseEntity.ok(
                ApiResponse.success("Intake time updated", response)
        );
    }
}
