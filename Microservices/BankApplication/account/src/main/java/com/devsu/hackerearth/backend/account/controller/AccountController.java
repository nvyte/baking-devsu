package com.devsu.hackerearth.backend.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll(){
		// api/accounts
		// Get all accounts
		return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id){
		// api/accounts/{id}
		// Get accounts by id
		return new ResponseEntity<>(accountService.getById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto){
		// api/accounts
		// Create accounts
		return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(accountDto)) ;
	}

	@PutMapping("{id}")
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto){
		// api/accounts/{id}
		// Update accounts
		return new ResponseEntity<>(accountService.update(accountDto), HttpStatus.OK);
	}

	@PatchMapping("{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id, @RequestBody PartialAccountDto partialAccountDto){
		// api/accounts/{id}
		// Partial update accounts
		return new ResponseEntity<>(accountService.partialUpdate(id, partialAccountDto), HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		// api/accounts/{id}
		// Delete accounts
		accountService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

