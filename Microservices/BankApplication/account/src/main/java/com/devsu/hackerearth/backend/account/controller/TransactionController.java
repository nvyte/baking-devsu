package com.devsu.hackerearth.backend.account.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping
	public ResponseEntity<List<TransactionDto>> getAll() {
		// api/transactions
		// Get all transactions
		var allTransactions = transactionService.getAll();
		return new ResponseEntity<>(allTransactions, HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<TransactionDto> get(@PathVariable Long id) {
		// api/transactions/{id}
		// Get transactions by id
		var allTransactions = transactionService.getById(id);
		return new ResponseEntity<>(allTransactions, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto transactionDto) {
		// api/transactions
		// Create transactions
		var transactionCreated = transactionService.create(transactionDto);
		return new ResponseEntity<>(transactionCreated, HttpStatus.CREATED);
	}

	@GetMapping("clients/{clientId}/report")
	public ResponseEntity<List<BankStatementDto>> report(@PathVariable Long clientId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionStart,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionEnd) {
		// api/transactions/clients/{clientId}/report
		// Get report
		var trx = transactionService.getAllByAccountClientIdAndDateBetween(clientId, dateTransactionStart,
				dateTransactionEnd);
		return new ResponseEntity<>(trx, HttpStatus.OK);
	}

	@GetMapping("{accountId}/last-transaction")
	public ResponseEntity<TransactionDto> getLastByAccountId(@PathVariable Long accountId) {
		return new ResponseEntity<>(transactionService.getLastByAccountId(accountId), HttpStatus.OK);
	}

}
