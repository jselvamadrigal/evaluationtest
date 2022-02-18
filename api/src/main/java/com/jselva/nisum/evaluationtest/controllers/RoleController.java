package com.jselva.nisum.evaluationtest.controllers;

import com.jselva.nisum.evaluationtest.data.dto.RoleDto;
import com.jselva.nisum.evaluationtest.data.entity.Role;
import com.jselva.nisum.evaluationtest.data.models.response.ResponseGenericApi;
import com.jselva.nisum.evaluationtest.services.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "${api.path}/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseGenericApi<RoleDto>> create(@Valid @RequestBody RoleDto request) {
        ResponseGenericApi<RoleDto> responseApi = new ResponseGenericApi<>();
        Optional<Role> roleOptional = this.roleService.save(request);

        roleOptional.ifPresent(role -> responseApi.setData(this.roleService.convertToDto(role)));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseApi);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READ')")
    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseGenericApi<RoleDto>> find(@Valid @RequestParam("name") String name) {
        ResponseGenericApi<RoleDto> responseApi = new ResponseGenericApi<>();
        Optional<Role> roleOptional = this.roleService.find(name);

        roleOptional.ifPresent(role -> responseApi.setData(this.roleService.convertToDto(role)));

        return ResponseEntity.status(HttpStatus.OK).body(responseApi);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_READ')")
    @SecurityRequirement(name = "JWT")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseGenericApi<List<RoleDto>>> getAll() {
        ResponseGenericApi<List<RoleDto>> responseApi = new ResponseGenericApi<>();
        List<Role> roles = this.roleService.getAll();

        responseApi.setData(roles.stream().map(this.roleService::convertToDto).collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.OK).body(responseApi);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping(value = "/{name}")
    public ResponseEntity<Object> remove(@Valid @PathVariable("name") String name) {
        this.roleService.remove(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}