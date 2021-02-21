package com.bilyoner.assignment.couponapi.controller;

import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/coupon-status")
    public List<CouponDTO> getAllCouponsByCouponStatus(@RequestParam CouponStatusEnum couponStatus) {
        return couponService.getAllCouponsByCouponStatus(couponStatus);
    }
    @PostMapping
    public CouponDTO createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
        return couponService.createCoupon(couponCreateRequest);
    }
    @GetMapping("/played-coupons/{userid}")
    public List<CouponDTO> getPlayedCoupons(@PathVariable("userid") Long userId) {
        return couponService.getPlayedCoupons(userId);
    }

    @PostMapping("/play-coupon")
    public List<CouponDTO> playCoupons(@Valid @RequestBody CouponPlayRequest couponPlayRequest) {
        return couponService.playCoupons(couponPlayRequest);
    }

    @PutMapping("/{couponId}")
    public CouponDTO cancelCoupon(@PathVariable("couponId") Long couponId) {
        return couponService.cancelCoupon(couponId);
    }
}