package com.halt.medtracker.medication_tracker_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationScheduleRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateScheduleRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationScheduleResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.MedicationScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class MedicationScheduleController {
    
    private final MedicationScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationScheduleResponseDTO>>
    createSchedule(@RequestBody CreateMedicationScheduleRequestDTO request) {

        MedicationScheduleResponseDTO response =
                scheduleService.createSchedule(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Schedule created", response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationScheduleResponseDTO>>
    updateSchedule(@PathVariable Long id , @RequestBody UpdateScheduleRequest request) {

        MedicationScheduleResponseDTO response =
                scheduleService.editSchedule(id,request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Schedule created", response));
    }

}
