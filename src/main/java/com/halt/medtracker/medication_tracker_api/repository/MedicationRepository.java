package com.halt.medtracker.medication_tracker_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Long>,JpaSpecificationExecutor<Medication>{
    // all medication of that user
    List<Medication> findByUserId(Long userId);
}
