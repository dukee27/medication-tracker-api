package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.stereotype.Service;

import com.halt.medtracker.medication_tracker_api.repository.MedicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationService {
    
    private final MedicationRepository medicationRepositery;

    


}
