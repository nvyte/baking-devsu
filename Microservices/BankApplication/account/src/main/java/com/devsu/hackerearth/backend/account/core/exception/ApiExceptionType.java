package com.devsu.hackerearth.backend.account.core.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiExceptionType {

    BAD_REQUEST("BEX_BAD_REQUEST", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND("BEX_NOT_FOUND", HttpStatus.NOT_FOUND),
    UNPROCESSABLE_OPERATION("BEX_UNPROCESSABLE", HttpStatus.UNPROCESSABLE_ENTITY),
    CLIENT_GATEWAY_ERROR("TEX_BAD_GATEWAY", HttpStatus.BAD_GATEWAY),
    SERVICE_UNAVAILABLE("TEX_SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_ERROR("TEX_INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final String codePrefix;
    private final HttpStatus httpStatus;

    public String generateCode() {
        return codePrefix + "_" + UUID.randomUUID();
    }
}
