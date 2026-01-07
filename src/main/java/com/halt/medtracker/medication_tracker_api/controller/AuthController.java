package com.halt.medtracker.medication_tracker_api.controller;

import com.halt.medtracker.medication_tracker_api.config.security.JwtTokenProvider;
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.LoginRequest;

import com.halt.medtracker.medication_tracker_api.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<com.halt.medtracker.medication_tracker_api.dto.ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        
        // 1. Authenticate user (Checks password hash automatically)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Set Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate Token
        String token = jwtTokenProvider.generateToken(authentication);

        // 4. Return Token
        return ResponseEntity.ok(ApiResponse.success(
                "Login successful",
                AuthResponse.builder().accessToken(token).build()
        ));
    }
}