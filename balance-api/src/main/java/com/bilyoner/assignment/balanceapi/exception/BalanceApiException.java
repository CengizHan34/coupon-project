package com.bilyoner.assignment.balanceapi.exception;

import lombok.Getter;

@Getter
public class BalanceApiException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public BalanceApiException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }
}
