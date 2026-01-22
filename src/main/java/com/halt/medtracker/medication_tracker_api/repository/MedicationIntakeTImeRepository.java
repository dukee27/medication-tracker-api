package com.halt.medtracker.medication_tracker_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halt.medtracker.medication_tracker_api.domain.medication.MedicationIntakeTime;

@Repository
public interface MedicationIntakeTimeRepository extends JpaRepository<MedicationIntakeTime,Long> {

    List<MedicationIntakeTime> findByScheduleId(Long ScheduleId);

}
