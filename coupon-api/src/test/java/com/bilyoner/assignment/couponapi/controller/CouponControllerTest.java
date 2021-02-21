package com.bilyoner.assignment.couponapi.controller;

import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CouponController.class)
public class CouponControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void getAllCouponsByCouponStatus_thenReturns200() throws Exception {
        CouponDTO couponDTO = CouponDTO.builder().id(1l).build();
        List<CouponDTO> couponEntities = Arrays.asList(couponDTO);
        doReturn(couponEntities).when(couponService).getAllCouponsByCouponStatus(any(CouponStatusEnum.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/coupon/coupon-status")
                .param("couponStatus","CREATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(couponDTO.getId().intValue())));
    }

    @Test
    public void createCoupon_thenReturns200() throws Exception{
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();

        CouponDTO couponDTO = CouponDTO.builder().id(1l).build();
        List<CouponDTO> couponEntities = Arrays.asList(couponDTO);
        doReturn(couponDTO).when(couponService).createCoupon(any(CouponCreateRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/coupon").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(couponCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(couponDTO.getId().intValue())));
    }

    @Test
    public void cancelCoupon_thenReturns200() throws Exception{
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest();

        CouponDTO couponDTO = CouponDTO.builder().id(1l).build();
        doReturn(couponDTO).when(couponService).cancelCoupon(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/coupon/{couponId}",1l))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(couponDTO.getId().intValue())));
    }
    @Test
    public void getPlayedCoupons_thenReturns200() throws Exception{
        CouponDTO couponDTO = CouponDTO.builder().id(1l).build();
        List<CouponDTO> couponEntities = Arrays.asList(couponDTO);

        doReturn(couponEntities).when(couponService).getPlayedCoupons(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/coupon/played-coupons/{userid}",1l))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(couponDTO.getId().intValue())));
    }

    @Test
    public void playCoupons_thenReturns200() throws Exception{
        CouponPlayRequest couponPlayRequest = new CouponPlayRequest();
        CouponDTO couponDTO = CouponDTO.builder().id(1l).build();
        List<CouponDTO> couponEntities = Arrays.asList(couponDTO);

        doReturn(couponEntities).when(couponService).playCoupons(any(CouponPlayRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/coupon/play-coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(couponPlayRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(couponDTO.getId().intValue())));
    }
}
