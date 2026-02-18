package com.halt.medtracker.medication_tracker_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.domain.medication.Medication;
import com.halt.medtracker.medication_tracker_api.dto.mapper.MedicationMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.MedicationFilterRequest;
import com.halt.medtracker.medication_tracker_api.dto.request.UpdateMedicationRequest;
import com.halt.medtracker.medication_tracker_api.exception.ResourceNotFoundException;
import com.halt.medtracker.medication_tracker_api.repository.MedicationRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    public Medication createMedication(CreateMedicationRequestDTO request, User subject) {

        Medication medication = medicationMapper.toEntity(request, subject);
        return medicationRepository.save(medication);
    }

    @Transactional
    public Medication editMedication(Long medId,
                                     UpdateMedicationRequest request,
                                     User subject) {

        Medication medication = medicationRepository.findById(medId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));

        if (!medication.getUser().getId().equals(subject.getId())) {
            throw new AccessDeniedException("Unauthorized access: You do not own this medication record");
        }

        medicationMapper.updateEntity(medication, request);

        return medicationRepository.save(medication);
    }

    public Medication getMedicationById(Long medId, User subject) {

        Medication medication = medicationRepository.findById(medId)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));

        if (!medication.getUser().getId().equals(subject.getId())) {
            throw new AccessDeniedException("Unauthorized access to this medication");
        }

        return medication;
    }

    public List<Medication> getAllUserMedications(User subject) {
        return medicationRepository.findByUserId(subject.getId());
    }

    @Transactional(readOnly = true)
    public Page<Medication> filterMedication(User subject,
                                             MedicationFilterRequest filter) {

        Specification<Medication> spec =
                MedicationSpecification.dynamicFilter(subject.getId(), filter);

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getPageSize() != null ? filter.getPageSize() : 10,
                Sort.by("startDate").descending()
        );

        return medicationRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public List<Medication> getLowStockReport(User subject) {

        MedicationFilterRequest filter = MedicationFilterRequest.builder()
                .isActive(true)
                .isLowStock(true)
                .build();

        Specification<Medication> spec =
                MedicationSpecification.dynamicFilter(subject.getId(), filter);

        return medicationRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public List<Medication> getExpiryReport(User subject) {

        LocalDate daysFromNow = LocalDate.now().plusDays(30);

        MedicationFilterRequest filter = MedicationFilterRequest.builder()
                .isActive(true)
                .expiryDateBefore(daysFromNow)
                .build();

        Specification<Medication> spec =
                MedicationSpecification.dynamicFilter(subject.getId(), filter);

        return medicationRepository.findAll(spec);
    }
}
