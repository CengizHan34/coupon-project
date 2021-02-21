package com.bilyoner.assignment.couponapi.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author created by cengizhan on 18.02.2021
 */
@Data
@Builder
public class UpdateBalanceRequest {
    private Long userId;
    private BigDecimal amount;
    private String transactionId;
    private String transactionType;
}
