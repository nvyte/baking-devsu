package com.devsu.hackerearth.backend.account;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class LastTransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private Account testAccount;

    private List<Transaction> trxs;

    @BeforeEach
    void beforeExec() {
        accountRepository.deleteAll();

        var account = new Account();
        account.setActive(true);
        account.setClientId(1l);
        account.setInitialAmount(0);
        account.setNumber("1234567890");
        account.setType("savings");
        testAccount = accountRepository.save(account);

        var trx1 = new Transaction();
        trx1.setAccountId(testAccount.getId());
        trx1.setAmount(99.0);
        trx1.setBalance(101);
        trx1.setDate(new Date());
        trx1.setType("deposit");
        var trx2 = new Transaction();
        trx2.setAccountId(testAccount.getId());
        trx2.setAmount(99.0);
        trx2.setBalance(101);
        trx2.setDate(new Date());
        trx2.setType("deposit");
        var trx3 = new Transaction();
        trx3.setAccountId(testAccount.getId());
        trx3.setAmount(99.0);
        trx3.setBalance(101);
        trx3.setDate(new Date());
        trx3.setType("deposit");

        var trxList = List.of(trx1, trx2, trx3);
        trxs = transactionRepository.saveAll(trxList)
                .stream().sorted(Comparator.comparing(Transaction::getId))
                .collect(Collectors.toList());
        trxs.forEach(System.out::println);
    }

    @AfterEach
    void afterExec() {
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    @Transactional
    void getLastTransaction() throws Exception {
        var lastTrx = trxs.get(trxs.size() - 1);
        this.mockMvc.perform(get("/api/transactions/" + testAccount.getId() + "/last-transaction"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                        matchAll(
                                jsonPath("$.id").value(3),
                                jsonPath("$.date").value(getDate(lastTrx.getDate())),
                                jsonPath("$.type").value("deposit"),
                                jsonPath("$.amount").value(99.0),
                                jsonPath("$.balance").value(101.0),
                                jsonPath("$.accountId").value(1)))
                .andDo(MockMvcResultHandlers.print());

    }

    private static String getDate(Date date) {
        var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(date);
    }
}
