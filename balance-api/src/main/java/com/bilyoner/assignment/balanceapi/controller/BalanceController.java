package com.bilyoner.assignment.balanceapi.controller;

import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PutMapping
    public void updateBalance(@Valid @RequestBody UpdateBalanceRequest updateBalanceRequest) {
        balanceService.updateBalance(updateBalanceRequest);
    }
}
