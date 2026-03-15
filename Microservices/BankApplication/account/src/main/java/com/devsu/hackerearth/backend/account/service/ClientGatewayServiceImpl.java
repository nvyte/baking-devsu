package com.devsu.hackerearth.backend.account.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsu.hackerearth.backend.account.core.ErrorPayload;
import com.devsu.hackerearth.backend.account.core.exception.ApiException;
import com.devsu.hackerearth.backend.account.core.exception.ApiExceptionType;
import com.devsu.hackerearth.backend.account.core.exception.BusinessException;
import com.devsu.hackerearth.backend.account.core.exception.TechnicalException;
import com.devsu.hackerearth.backend.account.model.dto.ClientDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientGatewayServiceImpl implements ClientGatewayService {

    private final RestTemplate restTemplate;
    private final String url;
    private final ObjectMapper objectMapper;

    public ClientGatewayServiceImpl(RestTemplate restTemplate,
            @org.springframework.beans.factory.annotation.Value("${api.client.service.base-url}") String clientUrl,
            ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.url = clientUrl;
        this.objectMapper = objectMapper;
    }

    public ClientDto getClientById(Long clientId) {
        log.debug("Searching for client wiht id {}", clientId);
        try {
            var endpointUrl = url + "api/clients/" + clientId;
            var response = restTemplate.getForEntity(endpointUrl, Map.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw resolveException(response);
            }
            return convertValue(response.getBody(), ClientDto.class);
        } catch (Exception e) {
            log.error("There's occurred an error when trying to get client", e);
            throw new TechnicalException(ApiExceptionType.INTERNAL_ERROR, "Error tratando de invocar client ms.", e);
        }
    }

    private String stringValueOf(Map body, String key, int code) {
        return Optional.ofNullable(body.get(key))
                .map(String::valueOf)
                .map(t -> code + t)
                .orElse("");
    }

    @SneakyThrows
    private <T> T convertValue(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    public ApiException resolveException(ResponseEntity<Map> response) {
        var statusCode = response.getStatusCodeValue();
        var body = response.getBody();
        var code = stringValueOf(body, "code", statusCode);
        var cause = stringValueOf(body, "cause", statusCode);
        if (statusCode == 404) {
            throw new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, code + " " + "cause");
        }
        if (statusCode == 400) {
            throw new BusinessException(ApiExceptionType.BAD_REQUEST, code + " " + "cause");
        }
        if (statusCode == 422) {
            throw new BusinessException(ApiExceptionType.UNPROCESSABLE_OPERATION, code + " " + "cause");
        }
        return new TechnicalException(ApiExceptionType.CLIENT_GATEWAY_ERROR,
                "Error tratando de invocar servicio" + " " + cause, null);
    }
}
