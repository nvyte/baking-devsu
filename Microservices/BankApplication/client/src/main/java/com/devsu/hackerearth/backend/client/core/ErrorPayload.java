package com.devsu.hackerearth.backend.client.core;

import java.time.LocalDateTime;

import com.devsu.hackerearth.backend.client.core.exception.ApiException;
import com.devsu.hackerearth.backend.client.core.exception.ApiExceptionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorPayload {
    private final String cause;
    private final String code;
    private final int status;
    private final String error;
    private final String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime dateTime;

    public static ErrorPayload fromApiException(ApiException exception, String path) {
        var type = exception.getType();
        return new ErrorPayload(
                exception.getMessage(),
                type.generateCode(),
                type.getHttpStatus().value(),
                type.getHttpStatus().getReasonPhrase(),
                path,
                LocalDateTime.now());
    }

    public static ErrorPayload fromUnknownException(ApiException exception, String path) {
        var type = ApiExceptionType.INTERNAL_ERROR;
        return new ErrorPayload(
                exception.getMessage(),
                type.generateCode(),
                type.getHttpStatus().value(),
                type.getHttpStatus().getReasonPhrase(),
                path,
                LocalDateTime.now());
    }

}
