package com.devsu.hackerearth.backend.account.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonMaperConfig {
    
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
