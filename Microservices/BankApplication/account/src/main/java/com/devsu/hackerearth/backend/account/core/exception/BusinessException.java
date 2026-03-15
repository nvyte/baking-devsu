package com.devsu.hackerearth.backend.account.core.exception;

public class BusinessException extends ApiException {

    public BusinessException(ApiExceptionType type, String message){
        super(type, message);
    }
}
