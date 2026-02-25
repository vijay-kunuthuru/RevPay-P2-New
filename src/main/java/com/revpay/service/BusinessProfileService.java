package com.revpay.service;

import com.revpay.exception.ResourceNotFoundException;
import com.revpay.exception.UnauthorizedException;
import com.revpay.model.dto.BusinessDocumentUploadRequest;
import com.revpay.model.entity.BusinessProfile;
import com.revpay.repository.BusinessProfileRepository;
import com.revpay.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BusinessProfileService {

    private final BusinessProfileRepository businessRepository;

    public void uploadVerificationDocument(Long businessId, BusinessDocumentUploadRequest request) {

        log.info("BUSINESS_DOC_UPLOAD_INIT | BusinessID: {}", businessId);

        // ==============================
        // Security Validation
        // ==============================

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Session invalid or unauthorized.");
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetailsImpl userDetails)) {
            throw new UnauthorizedException("Invalid authentication token format.");
        }

        if (!userDetails.getUserId().equals(businessId)) {
            throw new UnauthorizedException("You can upload document only for your own business.");
        }

        // ==============================
        // Fetch Business Profile
        // ==============================

        BusinessProfile profile = businessRepository.findByUser_UserId(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business profile not found"));

        // ==============================
        // Simulate File Upload
        // ==============================

        String simulatedPath =
                "/documents/business/" + businessId + "/" + request.getDocumentName();

        profile.setVerificationDocUrl(simulatedPath);
        profile.setVerified(false); // Re-verification required

        businessRepository.save(profile);

        log.info("BUSINESS_DOC_UPLOAD_SUCCESS | BusinessID: {} | Path: {}", businessId, simulatedPath);
    }
}