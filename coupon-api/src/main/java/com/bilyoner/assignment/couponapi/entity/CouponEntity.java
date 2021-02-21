package com.bilyoner.assignment.couponapi.entity;

import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CouponEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatusEnum status;

    @Column(nullable = false)
    private BigDecimal cost;

    private String transactionId;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime playDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createDate;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updateDate;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name = "COUPON_SELECTION",
            joinColumns = @JoinColumn(name = "COUPON_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
    private List<EventEntity> eventEntities;

}
