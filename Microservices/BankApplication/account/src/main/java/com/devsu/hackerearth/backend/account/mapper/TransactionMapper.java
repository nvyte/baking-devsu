package com.devsu.hackerearth.backend.account.mapper;

import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {
    
    public static Transaction mapToEntity(TransactionDto trxDto){
        Transaction trx = new Transaction();
        trx.setId(trxDto.getId());
        trx.setType(trxDto.getType());
        trx.setAccountId(trxDto.getAccountId());
        trx.setAmount(trxDto.getAmount());
        trx.setBalance(trxDto.getBalance());
        trx.setDate(trxDto.getDate());
        return trx;
    }

    public static TransactionDto mapToDto(Transaction trx){
        TransactionDto trxDto = new TransactionDto();
        trxDto.setId(trx.getId());
        trxDto.setType(trx.getType());
        trxDto.setAccountId(trx.getAccountId());
        trxDto.setAmount(trx.getAmount());
        trxDto.setBalance(trx.getBalance());
        trxDto.setDate(trx.getDate());
        return trxDto;
    }

}
