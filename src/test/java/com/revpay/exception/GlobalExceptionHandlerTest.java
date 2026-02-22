package com.revpay.exception;

import com.revpay.model.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleWalletRuntimeLimit() {
        RuntimeException ex = new RuntimeException("limit of INR50,000 exceeded");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleWalletRuntime(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        // FIXED: Reverted back to expecting LIMIT_ERR_01
        assertEquals("LIMIT_ERR_01", response.getBody().getErrorCode());
    }

    @Test
    void testHandleGeneralException() throws Exception {
        Exception ex = new Exception("Database failure");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/wallet/balance");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneral(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals("SYS_ERR", response.getBody().getErrorCode());
    }
}