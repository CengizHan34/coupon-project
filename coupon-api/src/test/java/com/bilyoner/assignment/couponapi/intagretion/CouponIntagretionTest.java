package com.bilyoner.assignment.couponapi.intagretion;

import com.bilyoner.assignment.couponapi.model.CouponDTO;
import org.bouncycastle.crypto.tls.CipherType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author created by cengizhan on 21.02.2021
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CouponIntagretionTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getAllCouponsByCouponStatus() throws Exception{
        ParameterizedTypeReference<List<CouponDTO>> responseType = new ParameterizedTypeReference<List<CouponDTO>>() {};

        RequestEntity request = RequestEntity
                .get(new URI("/api/coupon/coupon-status?couponStatus=CREATED"))
                .accept(MediaType.APPLICATION_JSON).build();

        ResponseEntity response = restTemplate.exchange(request,responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(),response.getBody());

    }

}
