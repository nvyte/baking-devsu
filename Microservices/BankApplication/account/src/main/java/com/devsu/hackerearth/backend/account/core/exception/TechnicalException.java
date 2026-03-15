package com.devsu.hackerearth.backend.account.core.exception;

public class TechnicalException extends ApiException {

    public TechnicalException(ApiExceptionType type, String message, Throwable cause){
        super(type, message, cause);
    }
}