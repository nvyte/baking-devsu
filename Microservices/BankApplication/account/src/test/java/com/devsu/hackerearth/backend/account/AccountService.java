package com.devsu.hackerearth.backend.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devsu.hackerearth.backend.account.service.ClientGatewayService;

@ExtendWith(MockitoExtension.class)
public class AccountService {

    @Mock
    private ClientGatewayService clientGatewayService;

    @InjectMocks
    private AccountService accountService;
    

    @Test
    void generateReport(){

    }
}
