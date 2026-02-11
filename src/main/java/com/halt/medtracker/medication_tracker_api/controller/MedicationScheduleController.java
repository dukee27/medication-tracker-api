package com.halt.medtracker.medication_tracker_api.controller;

import java.util.List;

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
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationScheduleRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateScheduleRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationScheduleResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.MedicationScheduleService;
import com.halt.medtracker.medication_tracker_api.service.SubjectResolver;
import com.halt.medtracker.medication_tracker_api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class MedicationScheduleController {
    
    private final MedicationScheduleService scheduleService;
    private final SubjectResolver subjectResolver;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationScheduleResponseDTO>>
    createSchedule(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @RequestBody CreateMedicationScheduleRequestDTO request) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.SCHEDULE_CREATE
        );

        MedicationScheduleResponseDTO response =
                scheduleService.createSchedule(request, subject);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Schedule created", response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationScheduleResponseDTO>>
    updateSchedule(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @PathVariable Long id,
            @RequestBody UpdateScheduleRequest request) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.SCHEDULE_EDIT
        );

        MedicationScheduleResponseDTO response =
                scheduleService.editSchedule(id, request, subject);

        return ResponseEntity.ok(
                ApiResponse.success("Schedule updated", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicationScheduleResponseDTO>>>
    getSchedules(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.SCHEDULE_VIEW
        );

        List<MedicationScheduleResponseDTO> response =
                scheduleService.getSchedules(subject);

        return ResponseEntity.ok(
                ApiResponse.success("Schedules fetched", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationScheduleResponseDTO>>
    getScheduleById(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long patientId,
            @PathVariable Long id) {

        User actor = userService.getUserByEmail(userDetails.getUsername());

        User subject = subjectResolver.resolveSubject(
                actor,
                patientId,
                Permissions.SCHEDULE_VIEW
        );

        MedicationScheduleResponseDTO response =
                scheduleService.getScheduleById(id, subject);

        return ResponseEntity.ok(
                ApiResponse.success("Schedule fetched", response)
        );
    }
}
