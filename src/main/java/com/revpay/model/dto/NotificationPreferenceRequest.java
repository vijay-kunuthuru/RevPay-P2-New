package com.revpay.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationPreferenceRequest {

    @NotBlank
    private String type;

    private boolean enabled; // true = enable, false = disable
}