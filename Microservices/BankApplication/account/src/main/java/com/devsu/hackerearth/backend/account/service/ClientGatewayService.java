package com.devsu.hackerearth.backend.account.service;

import com.devsu.hackerearth.backend.account.model.dto.ClientDto;

public interface ClientGatewayService {

    ClientDto getClientById(Long clientId);
    
}
