package com.halt.medtracker.medication_tracker_api.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.halt.medtracker.medication_tracker_api.dto.request.MedicationFilterRequest;
import com.halt.medtracker.medication_tracker_api.entity.Medication;

import jakarta.persistence.criteria.Predicate;

public class MedicationSpecification {
    public static Specification<Medication> dynamicFilter(
        Long userId,
        MedicationFilterRequest filter){
        return (root,query,cb)->{
            // root , tells which column to check here , root.get("field name") ig
            //cb -> criterion builder , ig its just engine for the logical part
            List<Predicate> predicates = new ArrayList<>();
            
            // user security , important!!! forces us to get data for our user , no one else
            predicates.add(cb.equal(root.get("user").get("id"),userId));
            
            // get medication name
            if(filter.getMedicineName() != null && !filter.getMedicineName().isBlank()){
                predicates.add(cb.like(
                    cb.lower(root.get("name")), "%" + filter.getMedicineName().toLowerCase() + "%"
                ));
            }

            // get doctor name 
            if(filter.getPrescribedBy() != null && !filter.getPrescribedBy().isBlank()){
                predicates.add(cb.like(
                    cb.lower(root.get("doctorName")),"%" + filter.getPrescribedBy().toLowerCase() + "%"
                ));
            }

            // if medication is active or not
            if(filter.getIsActive() != null){
                predicates.add(cb.equal(root.get("isActive"),filter.getIsActive()));
            }
            
            // med status 
            if(filter.getStatus() != null){
                predicates.add(cb.equal(root.get("status"),filter.getStatus()));
            }

            // meds that crossed expiry date
            if(Boolean.TRUE.equals(filter.getIsExpired())){
                predicates.add(cb.lessThan(root.get("expiryDate"),LocalDate.now()));
            }

            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
