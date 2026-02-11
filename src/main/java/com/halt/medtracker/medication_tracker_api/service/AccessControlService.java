package com.halt.medtracker.medication_tracker_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halt.medtracker.medication_tracker_api.constants.AccessStatus;
import com.halt.medtracker.medication_tracker_api.constants.RelationshipType;
import com.halt.medtracker.medication_tracker_api.domain.access.AccessControl;
import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.dto.mapper.AccessControlMapper;
import com.halt.medtracker.medication_tracker_api.dto.response.AccessResponseDTO;
import com.halt.medtracker.medication_tracker_api.repository.AccessControlRepository;
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final AccessControlRepository accessControlRepository;
    private final UserRepository userRepository;
    private final AccessControlMapper accessControlMapper;

    @Transactional
    public AccessResponseDTO requestAccess(User caregiver,
                                           String patientEmail,
                                           RelationshipType relationship) {

        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        AccessControl access = AccessControl.builder()
                .patient(patient)
                .caregiver(caregiver)
                .relationship(relationship)
                .status(AccessStatus.PENDING)
                .build();

        AccessControl saved = accessControlRepository.save(access);

        return accessControlMapper.toResponse(saved);
    }

    public List<AccessResponseDTO> getPendingRequests(User patient) {

        return accessControlRepository
                .findByPatientAndStatus(patient, AccessStatus.PENDING)
                .stream()
                .map(accessControlMapper::toResponse)
                .toList();
    }

    @Transactional
    public AccessResponseDTO approveRequest(User patient,
                                            Long accessId,
                                            Boolean canViewMeds,
                                            Boolean canEditMeds,
                                            Boolean canViewHistory) {

        AccessControl access = accessControlRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!access.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        access.setStatus(AccessStatus.APPROVED);
        access.setCanViewMeds(canViewMeds);
        access.setCanEditMeds(canEditMeds);
        access.setCanViewHistory(canViewHistory);

        AccessControl saved = accessControlRepository.save(access);

        return accessControlMapper.toResponse(saved);
    }

    @Transactional
    public AccessResponseDTO rejectRequest(User patient, Long accessId) {

        AccessControl access = accessControlRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!access.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        access.setStatus(AccessStatus.REJECTED);

        AccessControl saved = accessControlRepository.save(access);

        return accessControlMapper.toResponse(saved);
    }
}
