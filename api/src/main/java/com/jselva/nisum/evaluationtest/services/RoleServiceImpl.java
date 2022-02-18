package com.jselva.nisum.evaluationtest.services;

import com.jselva.nisum.evaluationtest.data.converter.RoleConverter;
import com.jselva.nisum.evaluationtest.data.dto.RoleDto;
import com.jselva.nisum.evaluationtest.data.entity.Role;
import com.jselva.nisum.evaluationtest.data.exceptions.ApiException;
import com.jselva.nisum.evaluationtest.data.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter converter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.converter = new RoleConverter();
    }

    @Transactional
    @Override
    public Optional<Role> save(RoleDto roleDto) {
        Optional<Role> roleOptional = this.roleRepository.findByName(roleDto.getName());

        roleOptional.ifPresent(role -> {
            throw new ApiException("role.found", HttpStatus.FORBIDDEN);
        });

        return Optional.of(this.roleRepository.save(convertToEntity(roleDto)));
    }

    @Override
    public Optional<Role> find(String roleName) {
        return Optional.ofNullable(this.roleRepository.findByName(roleName).orElseThrow(
                () -> new ApiException("role.not.found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = new ArrayList<>();
        this.roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    @Transactional
    @Override
    public void remove(String roleName) {
        Optional<Role> roleOptional = this.find(roleName);
        roleOptional.ifPresent(role -> this.roleRepository.delete(roleOptional.get()));
    }

    @Override
    public Role convertToEntity(RoleDto dto) {
        return this.converter.convertFromDto(dto);
    }

    @Override
    public RoleDto convertToDto(Role entity) {
        return this.converter.convertFromEntity(entity);
    }
}