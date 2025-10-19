package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.AccountDTO;
import com.daita.datn.models.entities.auth.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO(Account account);
}
