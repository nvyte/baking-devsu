package com.devsu.hackerearth.backend.account.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;

@Repository
public interface BankStatemetRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "select t.date AS date, a.number AS accountNumber, a.type AS accountType, a.initial_amount AS initialAmount, "
            + "a.is_active AS isActive, t.type AS transactionType, t.amount AS amount, t.balance AS balance "
            + "FROM accounts a INNER JOIN transactions t ON a.id = t.account_id "
            + "WHERE a.client_id = :clientId "
            + "AND t.date BETWEEN :startDate AND :endDate "
            + "ORDER BY t.date "
            + ";", nativeQuery = true)
    List<BankStatementDto> fetchAccountTransactionsByClient(
            @Param("clientId") Long clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
