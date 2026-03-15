package com.devsu.hackerearth.backend.client.core.exception;

public class BusinessException extends ApiException {

    public BusinessException(ApiExceptionType type, String message){
        super(type, message);
    }
}
