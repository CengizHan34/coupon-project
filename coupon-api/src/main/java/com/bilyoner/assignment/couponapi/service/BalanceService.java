package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.model.UpdateBalanceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final RestTemplate restTemplate;

    private static final String BALANCE_API_SERVICE = "http://localhost:9090/balances";

    public void updateBalance(UpdateBalanceRequest updateBalanceRequest) {

        ResponseEntity<Void> response = null;
        try {
            RequestEntity request = RequestEntity
                    .put(new URI(BALANCE_API_SERVICE))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(updateBalanceRequest);

            response = restTemplate.exchange(request, Void.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (response.getStatusCode().value() != HttpStatus.OK.value()) {
            throw new RuntimeException("Bakiye yetersiz olduğundan kupon alınamadı!");
        }
    }
}
