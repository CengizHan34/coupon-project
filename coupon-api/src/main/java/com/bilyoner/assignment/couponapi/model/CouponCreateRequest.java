package com.bilyoner.assignment.couponapi.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequest {
    private List<Long> eventIds;
}
