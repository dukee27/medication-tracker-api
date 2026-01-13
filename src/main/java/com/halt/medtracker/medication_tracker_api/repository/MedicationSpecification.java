package com.halt.medtracker.medication_tracker_api.repository;

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
                    cb.lower(root.get("medicineName")), "%" + filter.getMedicineName().toLowerCase() + "%"
                ));
            }

            // get doctor name 
            if(filter.getPrescribedBy() != null && !filter.getPrescribedBy().isBlank()){
                predicates.add(cb.like(
                    cb.lower(root.get("prescribedBy")),"%" + filter.getPrescribedBy().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
