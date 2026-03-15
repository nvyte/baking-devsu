package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementDto {
    
	private Date date;
	private String client;
	private String accountNumber;
	private String accountType;
	private double initialAmount;
    private boolean isActive;
	private String transactionType;
	private double amount;
	private double balance;
}
