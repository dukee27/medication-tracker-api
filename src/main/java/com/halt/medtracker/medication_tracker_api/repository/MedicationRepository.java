package com.halt.medtracker.medication_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halt.medtracker.medication_tracker_api.entity.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Long> {
    
}
