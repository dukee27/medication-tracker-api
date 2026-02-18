package com.halt.medtracker.medication_tracker_api.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;

import com.halt.medtracker.medication_tracker_api.constants.AccessStatus;
import com.halt.medtracker.medication_tracker_api.constants.Permissions;
import com.halt.medtracker.medication_tracker_api.domain.access.AccessControl;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.repository.AccessControlRepository;

@Service
@RequiredArgsConstructor
public class AccessAuthorizationService {

    private final AccessControlRepository accessControlRepository;

    public void authorize(User actor, User patient, Permissions permission) {

        // Self access always allowed
        if (actor.getId().equals(patient.getId())) {
            return;
        }

        AccessControl access = accessControlRepository
                .findByPatientAndCaregiverAndStatus(
                        patient,
                        actor,
                        AccessStatus.APPROVED
                )
                .orElseThrow(() ->
                        new AccessDeniedException("Access not granted")
                );

        switch (permission) {

            // Profile related
            case PROFILE_VIEW -> {
                if (!access.isCanViewHistory()) {
                    throw new AccessDeniedException("Not allowed to view profile");
                }
            }

            case PROFILE_EDIT -> {
                throw new AccessDeniedException("Caregivers cannot edit profile");
            }

            // Medication
            case MEDICATION_VIEW -> {
                if (!access.isCanViewMeds()) {
                    throw new AccessDeniedException("Not allowed to view medications");
                }
            }

            case MEDICATION_CREATE,
                 MEDICATION_EDIT,
                 MEDICATION_DELETE -> {
                if (!access.isCanEditMeds()) {
                    throw new AccessDeniedException("Not allowed to modify medications");
                }
            }

            // Schedule
            case SCHEDULE_VIEW -> {
                if (!access.isCanViewMeds()) {
                    throw new AccessDeniedException("Not allowed to view schedules");
                }
            }

            case SCHEDULE_CREATE,
                 SCHEDULE_EDIT,
                 SCHEDULE_DELETE -> {
                if (!access.isCanEditMeds()) {
                    throw new AccessDeniedException("Not allowed to modify schedules");
                }
            }

            // Intake time
            case INTAKE_TIME_VIEW -> {
                if (!access.isCanViewMeds()) {
                    throw new AccessDeniedException("Not allowed to view intake times");
                }
            }

            case INTAKE_TIME_CREATE,
                 INTAKE_TIME_EDIT,
                 INTAKE_TIME_DELETE -> {
                if (!access.isCanEditMeds()) {
                    throw new AccessDeniedException("Not allowed to modify intake times");
                }
            }

            default -> throw new AccessDeniedException("Unknown permission");
        }
    }
}
