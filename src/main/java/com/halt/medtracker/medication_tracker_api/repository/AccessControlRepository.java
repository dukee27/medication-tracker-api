package com.halt.medtracker.medication_tracker_api.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.halt.medtracker.medication_tracker_api.domain.access.AccessControl;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;

@Repository
public interface AccessControlRepository extends JpaRepository<AccessControl,Long>{
    Optional<AccessControl> findByPatientAndCaregiverAndAccessGrantedTrue(
            User patient,
            User caregiver
    );
}
