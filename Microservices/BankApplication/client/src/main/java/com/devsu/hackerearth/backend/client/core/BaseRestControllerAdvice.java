package com.devsu.hackerearth.backend.client.core;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devsu.hackerearth.backend.client.core.exception.ApiException;
import com.devsu.hackerearth.backend.client.core.exception.ApiExceptionType;

@RestControllerAdvice
public class BaseRestControllerAdvice {

    private static final Logger log = Logger.getLogger(BaseRestControllerAdvice.class.getName());

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorPayload> handleBusinessException(ApiException ex, HttpServletRequest request) {
        log.info("Exception ocurred... catching on adviser... " + ex.fillInStackTrace());
        return buildRepsonse(ex, request);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorPayload> handleUnknownException(Throwable ex, HttpServletRequest request) {
        log.info("Exception ocurred... catching on adviser... " + ex.fillInStackTrace());
        return buildRepsonse(ex, request);
    }

    private ResponseEntity<ErrorPayload> buildRepsonse(Throwable throwable, HttpServletRequest request) {
        var type = resolveExceptionType(throwable);
        var payload = throwable instanceof ApiException
                ? ErrorPayload.fromApiException((ApiException) throwable, request.getRequestURI())
                : ErrorPayload.fromUnknownException((ApiException) throwable, request.getRequestURI());
        return new ResponseEntity<>(payload, type.getHttpStatus());
    }

    public ApiExceptionType resolveExceptionType(Throwable throwable) {
        if (throwable instanceof ApiException) {
            return ((ApiException) throwable).getType();
        }
        return ApiExceptionType.INTERNAL_ERROR;
    }

}
