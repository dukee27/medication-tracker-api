package com.halt.medtracker.medication_tracker_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationScheduleRequest;
import com.halt.medtracker.medication_tracker_api.dto.response.TodayMedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.TodayMedicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class MedicationScheduleController {

    private final TodayMedicationService todayMedicationService;
    
    // @PostMapping
    // public ResponseEntity<ApiResponse<TodayMedicationResponseDTO>> createSchedule(
    //     @RequestBody CreateMedicationScheduleRequest request){

    //     }

}
