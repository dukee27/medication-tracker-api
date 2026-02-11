package com.halt.medtracker.medication_tracker_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessApprovalDTO {

    @NotNull
    private Boolean canViewMeds;

    @NotNull
    private Boolean canEditMeds;

    @NotNull
    private Boolean canViewHistory;
}
