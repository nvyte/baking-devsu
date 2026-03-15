package com.devsu.hackerearth.backend.account.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.core.exception.ApiExceptionType;
import com.devsu.hackerearth.backend.account.core.exception.BusinessException;
import com.devsu.hackerearth.backend.account.mapper.AccountMapper;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
        return accountRepository.findById(id)
                .map(AccountMapper::mapToDto)
                .orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Account not found"));
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        var accounEntity = AccountMapper.mapToEntity(accountDto);
        var accountSaved = accountRepository.save(accounEntity);
        return AccountMapper.mapToDto(accountSaved);
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        // Update account
        validate(accountDto);
        var accountId = accountDto.getId();
        var accoutFound = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Account not found"));

        var newAccountValues = AccountMapper.mapToEntity(accountDto);
        AccountMapper.mapPartialUpdate(newAccountValues, accoutFound);
        return AccountMapper.mapToDto(accoutFound);
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        validate(partialAccountDto);
        var accoutFound = accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Account not found"));
        // create a method mapper if appears new variables
        accoutFound.setActive(partialAccountDto.isActive());
        return AccountMapper.mapToDto(accoutFound);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.findById(id)
                .map(account -> {
                    accountRepository.deleteById(account.getId());
                    return account;
                })
                .orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Account not found"));
        ;
    }

    private void validate(AccountDto accountDto) {
        if (Objects.isNull(accountDto)) {
            throw new BusinessException(ApiExceptionType.BAD_REQUEST, "Account is null");
        }
        var accountId = accountDto.getId();
        if (Objects.isNull(accountId)) {
            throw new BusinessException(ApiExceptionType.BAD_REQUEST, "Account ID is null");
        }
    }

    private void validate(PartialAccountDto accountDto) {
        if (Objects.isNull(accountDto)) {
            throw new BusinessException(ApiExceptionType.BAD_REQUEST, "Partial Account is null");
        }
    }

}
