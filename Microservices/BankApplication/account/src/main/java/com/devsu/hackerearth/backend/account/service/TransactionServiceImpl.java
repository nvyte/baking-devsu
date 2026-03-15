package com.devsu.hackerearth.backend.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.core.exception.ApiExceptionType;
import com.devsu.hackerearth.backend.account.core.exception.BusinessException;
import com.devsu.hackerearth.backend.account.mapper.TransactionMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.BankStatemetRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = Logger.getLogger(TransactionServiceImpl.class.getName());
    private final TransactionRepository transactionRepository;
    private final BankStatemetRepository bankStatemetRepository;
    private final AccountRepository accountRepository;
    private final ClientGatewayService clientGatewayService;

    @Override
    public List<TransactionDto> getAll() {
        // Get all transactions
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        // Get transactions by id
        return transactionRepository.findById(id)
                .map(TransactionMapper::mapToDto)
                .orElseThrow(
                        () -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Transaction not found."));
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        // Create transaction
        var accountId = Optional.ofNullable(transactionDto)
                .map(TransactionDto::getAccountId)
                .filter(Objects::nonNull)
                .orElseThrow(
                        () -> new BusinessException(ApiExceptionType.BAD_REQUEST, "transaction or accountId is null"));

        var accountFound = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ApiExceptionType.RESOURCE_NOT_FOUND, "Account not found."));

        var trxType = transactionDto.getType();
        if ("deposit".equals(trxType)) {
            calculateDeposit(accountFound, transactionDto);
        } else if ("withdraw".equals(trxType)) {
            calculateWithdraw(accountFound, transactionDto);
        }
        accountRepository.save(accountFound);
        return Optional.ofNullable(transactionDto)
                .map(TransactionMapper::mapToEntity)
                .map(transactionRepository::save)
                .map(TransactionMapper::mapToDto)
                .orElseThrow();
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(
            Long clientId,
            Date dateTransactionStart,
            Date dateTransactionEnd) {
        var client = clientGatewayService.getClientById(clientId);
        var startDate = dateTransactionStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var endDate = dateTransactionEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return bankStatemetRepository.fetchAccountTransactionsByClient(clientId, startDate, endDate)
                .stream()
                .peek(item -> item.setClient(client.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        var trx = transactionRepository.findFirstByAccountIdOrderByIdDesc(accountId);
        return TransactionMapper.mapToDto(trx);
    }

    public void calculateDeposit(Account accountFound, TransactionDto trx) {
        var trxAmount = trx.getAmount();
        var balance = accountFound.getInitialAmount();
        if (trxAmount <= 0.0)
            throw new BusinessException(ApiExceptionType.BAD_REQUEST, "Monto invalido para deposito.");
        var newBalance = balance + trxAmount;
        accountFound.setInitialAmount(newBalance);
        trx.setBalance(newBalance);
    }

    public void calculateWithdraw(Account accountFound, TransactionDto trx) {
        var trxAmount = trx.getAmount();
        var balance = accountFound.getInitialAmount();
        if (balance < 0.0)
            throw new BusinessException(ApiExceptionType.UNPROCESSABLE_OPERATION, "Saldo no disponible.");
        var newBalance = balance + trxAmount;
        newBalance = BigDecimal.valueOf(newBalance).setScale(2, RoundingMode.HALF_UP).doubleValue();
        accountFound.setInitialAmount(newBalance);
        trx.setBalance(newBalance);
    }
}
