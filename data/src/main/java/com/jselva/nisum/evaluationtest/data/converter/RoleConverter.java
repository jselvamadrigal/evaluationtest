package com.jselva.nisum.evaluationtest.data.converter;

import com.jselva.nisum.evaluationtest.data.dto.RoleDto;
import com.jselva.nisum.evaluationtest.data.entity.Role;
import com.jselva.nisum.evaluationtest.data.util.Converter;

public class RoleConverter extends Converter<RoleDto, Role> {

    public RoleConverter() {
        super(RoleConverter::convertToEntity, RoleConverter::convertToDto);
    }

    private static RoleDto convertToDto(Role role) {
        return new RoleDto(role.getId(), role.getName(), role.getDescription());
    }

    private static Role convertToEntity(RoleDto dto) {
        return new Role(dto.getId(), dto.getName(), dto.getDescription());
    }
}
