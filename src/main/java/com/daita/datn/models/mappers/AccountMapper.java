package com.daita.datn.models.mappers;

import com.daita.datn.enums.AccountStatus;
import com.daita.datn.models.dto.AccountDTO;
import com.daita.datn.models.dto.auth.RegisterRequestDTO;
import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.models.entities.auth.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface AccountMapper {
    @Mapping(source = "createAt", target = "createdAt")
    AccountDTO toDTO(Account account);

    default Account mapRegisterDtoToEntity(RegisterRequestDTO registerDTO, Role role) {
        Account account = new Account();
        account.setEmail(registerDTO.getEmail());
        account.setPassword(registerDTO.getPassword());
        account.setStatus(AccountStatus.ACTIVE);
        if (role != null) {
            account.setRoles(Set.of(role));
        }
        return account;
    }
}
