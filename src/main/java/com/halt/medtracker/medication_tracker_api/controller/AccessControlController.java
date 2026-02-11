package com.halt.medtracker.medication_tracker_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halt.medtracker.medication_tracker_api.domain.identity.User;
import com.halt.medtracker.medication_tracker_api.dto.ApiResponse;
import com.halt.medtracker.medication_tracker_api.dto.request.AccessApprovalDTO;
import com.halt.medtracker.medication_tracker_api.dto.request.AccessRequestDTO;
import com.halt.medtracker.medication_tracker_api.dto.response.AccessResponseDTO;
import com.halt.medtracker.medication_tracker_api.service.AccessControlService;
import com.halt.medtracker.medication_tracker_api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/access")
@RequiredArgsConstructor
public class AccessControlController {

    private final AccessControlService accessControlService;
    private final UserService userService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<AccessResponseDTO>> requestAccess(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AccessRequestDTO request) {

        User caregiver = userService.getUserByEmail(userDetails.getUsername());

        AccessResponseDTO response =
                accessControlService.requestAccess(
                        caregiver,
                        request.getPatientEmail(),
                        request.getRelationship()
                );

        return ResponseEntity.ok(
                ApiResponse.success("Access request sent", response)
        );
    }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<List<AccessResponseDTO>>> getPendingRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        User patient = userService.getUserByEmail(userDetails.getUsername());

        List<AccessResponseDTO> response =
                accessControlService.getPendingRequests(patient);

        return ResponseEntity.ok(
                ApiResponse.success("Pending requests", response)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<AccessResponseDTO>> approveRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody AccessApprovalDTO request) {

        User patient = userService.getUserByEmail(userDetails.getUsername());

        AccessResponseDTO response =
                accessControlService.approveRequest(
                        patient,
                        id,
                        request.getCanViewMeds(),
                        request.getCanEditMeds(),
                        request.getCanViewHistory()
                );

        return ResponseEntity.ok(
                ApiResponse.success("Access approved", response)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<AccessResponseDTO>> rejectRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        User patient = userService.getUserByEmail(userDetails.getUsername());

        AccessResponseDTO response =
                accessControlService.rejectRequest(patient, id);

        return ResponseEntity.ok(
                ApiResponse.success("Access rejected", response)
        );
    }
}
