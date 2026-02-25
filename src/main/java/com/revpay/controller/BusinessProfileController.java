package com.revpay.controller;

import com.revpay.model.dto.ApiResponse;
import com.revpay.model.dto.BusinessDocumentUploadRequest;
import com.revpay.service.BusinessProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
@Tag(name = "Business Profile", description = "Endpoints for managing business profile and verification documents")
public class BusinessProfileController {

    private final BusinessProfileService service;

    @PostMapping("/{businessId}/upload-document")
    @PreAuthorize("hasRole('BUSINESS')")
    @Operation(summary = "Upload verification document (Simulated)",
            description = "Uploads a verification document for business account approval (simulated storage).")
    public ResponseEntity<ApiResponse<String>> uploadDocument(
            @PathVariable Long businessId,
            @RequestBody BusinessDocumentUploadRequest request) {

        log.info("Received document upload request for Business ID: {}", businessId);

        service.uploadVerificationDocument(businessId, request);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Verification document uploaded successfully (simulated)."));
    }
}