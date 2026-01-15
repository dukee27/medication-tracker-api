package com.halt.medtracker.medication_tracker_api.dto.mapper;

import org.springframework.stereotype.Component;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.dto.response.UserResponseDTO;

@Component
public class UserMapper {
    public UserResponseDTO toResponse(User user){
        return UserResponseDTO.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
