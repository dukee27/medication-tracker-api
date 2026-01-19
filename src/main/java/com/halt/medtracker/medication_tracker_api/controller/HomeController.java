package com.halt.medtracker.medication_tracker_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.response.TodayMedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.TodayMedicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final TodayMedicationService todayMedicationService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<TodayMedicationResponseDTO>>> getTodayMedications() {

        List<TodayMedicationResponseDTO> response =
                todayMedicationService.getMedicationsForToday();

        return ResponseEntity.ok(
                ApiResponse.success("Today's medications", response)
        );
    }
}