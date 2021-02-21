package com.bilyoner.assignment.couponapi.service;


import com.bilyoner.assignment.couponapi.model.UpdateBalanceRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BalanceServiceTest {
    private static final String BALANCE_API_SERVICE = "http://localhost:9090/balances";

    @Mock
    private RestTemplate restTemplate;

    private BalanceService balanceService;

    @BeforeEach
    void setUp(){
        balanceService = new BalanceService(restTemplate);
    }

    @Test
    public void updateBalance_whenUpdateBalanceIsCalled_shouldNotThrowAnException(){
        UpdateBalanceRequest updateBalanceRequest = UpdateBalanceRequest.builder().build();

        try {
            RequestEntity request = RequestEntity
                    .put(new URI(BALANCE_API_SERVICE))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(updateBalanceRequest);
            when(restTemplate.exchange(request, Void.class)).thenReturn(new ResponseEntity(Void.class, HttpStatus.OK));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        balanceService.updateBalance(updateBalanceRequest);
    }

    @Test()
    public void updateBalance_ifTheRestTemplateTimeoutFalls_throwException(){
        UpdateBalanceRequest updateBalanceRequest = UpdateBalanceRequest.builder().build();

        try {
            RequestEntity request = RequestEntity
                    .put(new URI(BALANCE_API_SERVICE))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(updateBalanceRequest);
            when(restTemplate.exchange(request, Void.class)).thenReturn(new ResponseEntity(Void.class, HttpStatus.GATEWAY_TIMEOUT));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Assertions.assertThrows(RuntimeException.class, () -> {
            balanceService.updateBalance(updateBalanceRequest);
        });
    }
}
