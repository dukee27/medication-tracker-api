package com.halt.medtracker.medication_tracker_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.mapper.UserMapper;
import com.halt.medtracker.medication_tracker_api.dto.request.CreateUserRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.response.UserResponseDTO;
import com.halt.medtracker.medication_tracker_api.entity.User;
import com.halt.medtracker.medication_tracker_api.exception.ResourceNotFoundException;
import com.halt.medtracker.medication_tracker_api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('USER')")
public class UserController {

    
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(
        @Valid @RequestBody CreateUserRequestDTO request) {
            User createdUser = userService.createUser(request);
            UserResponseDTO userResponse = userMapper.toResponse(createdUser);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully", userResponse));
        }
    
    
    @GetMapping("me")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // If security works, userDetails will NOT be null
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                ApiResponse.success("Profile fetched", userMapper.toResponse(user))
        );
    }

}
