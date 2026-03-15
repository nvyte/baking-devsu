package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

	private Long id;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date date;
	private String type;
	private double amount;
	private double balance;
	private Long accountId;
}
