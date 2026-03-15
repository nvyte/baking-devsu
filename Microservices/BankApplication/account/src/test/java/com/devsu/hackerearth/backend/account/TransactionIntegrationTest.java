package com.devsu.hackerearth.backend.account;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void afterExec() {
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    @Transactional
    void createDepositTransaction() throws Exception {

        var account = new Account();
        account.setActive(true);
        account.setClientId(1l);
        account.setInitialAmount(0);
        account.setNumber("1234567890");
        account.setType("savings");
        var testAccount = accountRepository.save(account);

        var trx = new TransactionDto();
        trx.setAccountId(testAccount.getId());
        trx.setAmount(99.0);
        trx.setBalance(101);
        trx.setDate(new Date());
        trx.setType("deposit");

        var objectMapper = new ObjectMapper();

        var payload = objectMapper.writeValueAsString(trx);
        this.mockMvc.perform(post("/api/transactions")
                .content(payload).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                        matchAll(
                                jsonPath("$.id").value(1),
                                jsonPath("$.date").value(getDate(trx.getDate())),
                                jsonPath("$.type").value("deposit"),
                                jsonPath("$.amount").value(99.0),
                                jsonPath("$.balance").value(99.0),
                                jsonPath("$.accountId").value(1)))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Transactional
    void createWithdrawTransaction() throws Exception {
        accountRepository.deleteAll();

        var account = new Account();
        account.setActive(true);
        account.setClientId(1l);
        account.setInitialAmount(99.0);
        account.setNumber("1234567890");
        account.setType("savings");
        var testAccount = accountRepository.save(account);

        var trx = new TransactionDto();
        trx.setAccountId(testAccount.getId());
        trx.setAmount(-98.9);
        trx.setBalance(99.0);
        trx.setDate(new Date());
        trx.setType("withdraw");

        var objectMapper = new ObjectMapper();

        var payload = objectMapper.writeValueAsString(trx);
        this.mockMvc.perform(post("/api/transactions")
                .content(payload).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(matchAll(
                        jsonPath("$.id").value(2),
                        jsonPath("$.date").value(getDate(trx.getDate())),
                        jsonPath("$.type").value("withdraw"),
                        jsonPath("$.amount").value(-98.9),
                        jsonPath("$.balance").value(0.1),
                        jsonPath("$.accountId").value(2)))
                .andDo(MockMvcResultHandlers.print());

    }

    private static String getDate(Date date) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(date);
    }
}
