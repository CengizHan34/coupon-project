package com.bilyoner.assignment.balanceapi.persistence.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceEntity {

    @Id
    @GeneratedValue
    private Long userId;
    private BigDecimal amount;
}
