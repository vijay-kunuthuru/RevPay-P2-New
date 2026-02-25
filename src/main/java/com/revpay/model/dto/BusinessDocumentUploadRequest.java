package com.revpay.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BusinessDocumentUploadRequest {

    @NotBlank(message = "Document name is required")
    private String documentName;

    @NotBlank(message = "Document type is required")
    private String documentType; // GST, PAN, LICENSE etc
}