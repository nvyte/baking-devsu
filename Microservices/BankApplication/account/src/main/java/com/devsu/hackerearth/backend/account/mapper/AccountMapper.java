package com.devsu.hackerearth.backend.account.mapper;

import java.util.Objects;

import org.apache.logging.log4j.util.Strings;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {
    
    public static Account mapToEntity(AccountDto accountDto) {
        Account accountEntity = new Account();
        accountEntity.setId(accountDto.getId());
        accountEntity.setNumber(accountDto.getNumber());
        accountEntity.setType(accountDto.getType());
        accountEntity.setInitialAmount(accountDto.getInitialAmount());
        accountEntity.setClientId(accountDto.getClientId());
        accountEntity.setActive(accountDto.isActive());
        return accountEntity;
    }

    public static AccountDto mapToDto(Account account) {
        AccountDto accountResponse = new AccountDto();
        accountResponse.setId(account.getId());
        accountResponse.setNumber(account.getNumber());
        accountResponse.setType(account.getType());
        accountResponse.setInitialAmount(account.getInitialAmount());
        accountResponse.setClientId(account.getClientId());
        accountResponse.setActive(account.isActive());
        return accountResponse;
    }

    
    public static void mapPartialUpdate(Account origin, Account target) {
        if (Objects.nonNull(origin) && Objects.nonNull(target)) {
            if (origin.getId() != null) {
                target.setId(origin.getId());
            }
            if (origin.getNumber() != null && !Strings.isBlank(origin.getNumber())) {
                target.setNumber(origin.getNumber());
            }
            if (origin.getType() != null && !Strings.isBlank(origin.getType())) {
                target.setType(origin.getType());
            }

            target.setClientId(origin.getClientId());
            target.setActive(origin.isActive());

        }
    }
}
