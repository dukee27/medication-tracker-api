package com.halt.medtracker.medication_tracker_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.BeanRegistry.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halt.medtracker.medication_tracker_api.dto.request.CreateMedicationRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.response.MedicationResponseDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.MedicationFilterRequest;
import com.halt.medtracker.medication_tracker_api.entity.Medication;
import com.halt.medtracker.medication_tracker_api.entity.User;
import com.halt.medtracker.medication_tracker_api.repository.MedicationRepository;
import com.halt.medtracker.medication_tracker_api.repository.MedicationSpecification;
import com.halt.medtracker.medication_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicationService {
    
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    public Medication createMedication(CreateMedicationRequestDTO request){

        //current logged-in user's email from the JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

  
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                
        Medication medication = Medication.builder()
                                .user(currentUser)
                                .name(request.getName())
                                .brandName(request.getBrandName())
                                .dosage(request.getDosage()) 
                                .quantityTotal(request.getQuantity()) 
                                .quantityLeft(request.getQuantity())
                                .type(request.getType())
                                .doctorName(request.getDoctorName())
                                .instructions(request.getInstructions())
                                .imageUrl(request.getImageUrl())
                                .expiryDate(request.getExpiryDate())
                                .startDate(request.getStartDate())
                                .endDate(request.getEndDate()) 
                                .isActive(true) 
                                .build();
        
        return medicationRepository.save(medication);
                                
    }

    public List<Medication> getAllUserMedications(){
        User user = getCurrentUser();
        return medicationRepository.findByUserId(user.getId());
    }

    public Medication getMedicationById(Long medId){
        User user = getCurrentUser();

        Medication med = medicationRepository.findById(medId)
                        .orElseThrow(()->new RuntimeException("medication not found"));
        
        if(!med.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized access to medication");
        }
        return med;
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public Page<MedicationResponseDTO> filterMedication(Long userId, MedicationFilterRequest filter){

        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found");
        }

        Specification<Medication> spec = MedicationSpecification.dynamicFilter(userId,filter);

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getPageSize() != null ? filter.getPageSize() : 10,
                Sort.by("startDate").descending()
        );
        
        Page<Medication>  medications = medicationRepository.findAll(spec,pageable);

        return medications.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDTO> getMedicationsForToday(Long userId) {
        MedicationFilterRequest filter = MedicationFilterRequest.builder()
                .isActive(true)
                .isDueToday(true)
                .isExpired(false)
                .sortBy("startDate")
                .sortOrder("ASC")
                .build();

        Specification<Medication> spec = MedicationSpecification.dynamicFilter(userId, filter);
        
        return medicationRepository.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDTO> getLowStockReport(Long userId){
        MedicationFilterRequest filter = MedicationFilterRequest.builder()
                .isActive(true)
                .isLowStock(true)
                .sortBy("startDate")
                .sortOrder("ASC")
                .build();
        
                Specification<Medication> spec = MedicationSpecification.dynamicFilter(userId, filter);
        
        return medicationRepository.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDTO> getExpiryReport(Long userId) {
        
        LocalDate DaysFromNow = LocalDate.now().plusDays(30);

        MedicationFilterRequest filter = MedicationFilterRequest.builder()
                .isActive(true)
                .expiryDateBefore(DaysFromNow) 
                .sortBy("expiryDate") 
                .sortOrder("ASC")
                .build();

        Specification<Medication> spec = MedicationSpecification.dynamicFilter(userId, filter);

        return medicationRepository.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    private MedicationResponseDTO toResponse(Medication med){
        return MedicationResponseDTO.builder()
                .id(med.getId())
            
                .name(med.getName())  
                
                .brandName(med.getBrandName())
                .dosage(med.getDosage())
                .type(med.getType())           
                
                .startDate(med.getStartDate())
                .endDate(med.getEndDate())     
                
                .doctorName(med.getDoctorName()) 
                
                .isActive(med.isActive())
                .quantityLeft(med.getQuantityLeft())
                .expiryDate(med.getExpiryDate())
                
                .instructions(med.getInstructions()) 
                .imageUrl(med.getImageUrl())         
                
                .build();
    }
}
