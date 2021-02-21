package com.bilyoner.assignment.balanceapi.persistence.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceHistoryEntity {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private Long userId;
    @NotBlank
    private BigDecimal amount;
    @NotBlank
    private String transactionId;
    @NotBlank
    private String transactionType;
}
