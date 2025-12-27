package com.daita.datn.models.mappers;

import com.daita.datn.models.dto.auth.RoleDTO;
import com.daita.datn.models.entities.auth.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    RoleDTO toDto(Role role);
}
