package com.bilyoner.assignment.couponapi.model;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponPlayRequest {

    private Long userId;
    private List<Long> couponIds;
}
