package com.revpay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanAnalyticsDTO {
    private BigDecimal totalOutstanding;
    private BigDecimal totalPaid;
    private BigDecimal totalPending;
}