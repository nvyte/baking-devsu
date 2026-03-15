package com.devsu.hackerearth.backend.account;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.ResultHandler.*;
import static org.springframework.test.web.servlet.ResultMatcher.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionReportIntegrationTest {

    // List<BankStatementDto> getAllByAccountClientIdAndDateBetween(
    // Long clientId,
    // @Param("dateTransactionStart") Date dateTransactionStart,
    // @Param("dateTransactionEnd") Date dateTransactionEnd);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void afterExec() {
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    @Transactional
    void generateReport() throws Exception {

        // //
        // private Date date;
        // private String client;
        // private String accountNumber;
        // private String accountType;
        // private double initialAmount;
        // private boolean isActive;
        // private String transactionType;
        // private double amount;
        // private double balance;

        // acct
        // private String number;
        // private String type;
        // private double initialAmount;
        // private boolean isActive;

        // trx
        // private Date date;
        // private String type;
        // private double amount;
        // private double balance;

        // @Column(name = "account_id")
        // private Long accountId;

        var account1 = new Account();
        account1.setActive(true);
        account1.setClientId(1l);
        account1.setInitialAmount(1000);
        account1.setNumber("111111111");
        account1.setType("savings");

        var account2 = new Account();
        account2.setActive(true);
        account2.setClientId(1l);
        account2.setInitialAmount(100);
        account2.setNumber("2222222222");
        account2.setType("savings");

        var account3 = new Account();
        account3.setActive(true);
        account3.setClientId(1l);
        account3.setInitialAmount(300);
        account3.setNumber("3333333333");
        account3.setType("credit");

        List<Account> testAccount = accountRepository.saveAll(List.of(account1, account2, account3));

        var trx1 = new Transaction();
        trx1.setAccountId(testAccount.get(0).getId());
        trx1.setAmount(99.0);
        trx1.setBalance(101);
        var trxDate1 = LocalDate.now().minusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant();
        trx1.setDate(Date.from(trxDate1));
        trx1.setType("deposit");

        var trx2 = new Transaction();
        trx2.setAccountId(testAccount.get(1).getId());
        trx2.setAmount(99.0);
        trx2.setBalance(101);
        trx2.setDate(new Date());
        trx2.setType("deposit");

        var trx3 = new Transaction();
        trx3.setAccountId(testAccount.get(2).getId());
        trx3.setAmount(99.0);
        trx3.setBalance(101);
        var trxDate3 = LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant();
        trx3.setDate(Date.from(trxDate3));
        trx3.setType("deposit");

        var trxsToSave = List.of(trx1, trx2, trx3);
        transactionRepository.saveAll(trxsToSave);
        this.mockMvc
                .perform(get("/api/transactions/clients/" + 1 + "/report?dateTransactionStart=" + trxDate1
                        + "&dateTransactionEnd=" + trxDate3))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(matchAll(
                        jsonPath("$[0].date").isNotEmpty(),
                        jsonPath("$[0].client").isNotEmpty(),
                        jsonPath("$[0].accountNumber").value(111111111),
                        jsonPath("$[0].accountType").value("savings"),
                        jsonPath("$[0].initialAmount").value(1000),
                        jsonPath("$[0].isActive").value(true),
                        jsonPath("$[0].transactionType").value("deposit"),
                        jsonPath("$[0].amount").value(99.0),
                        jsonPath("$[0].balance").value(101)));
    }

}
