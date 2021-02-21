package com.bilyoner.assignment.couponapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {GenericException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage resourceNotFoundException(GenericException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(ErrorCodeEnum.FIELD_VALIDATION_ERROR.getCode(),
                new Date(), ex.getMessage(),
                request.getDescription(false));

        return message;
    }

}