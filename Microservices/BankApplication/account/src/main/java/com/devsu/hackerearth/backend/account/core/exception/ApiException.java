package com.devsu.hackerearth.backend.account.core.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ApiExceptionType type;

    public ApiException(ApiExceptionType type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public ApiException(ApiExceptionType type, String message) {
        super(message, null);
        this.type = type;
    }
}
