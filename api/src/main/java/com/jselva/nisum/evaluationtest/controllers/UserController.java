package com.jselva.nisum.evaluationtest.controllers;

import com.jselva.nisum.evaluationtest.data.dto.UserDto;
import com.jselva.nisum.evaluationtest.data.dto.UserRequestRegisterDto;
import com.jselva.nisum.evaluationtest.data.entity.User;
import com.jselva.nisum.evaluationtest.data.models.LoginRequest;
import com.jselva.nisum.evaluationtest.data.models.response.ResponseGenericApi;
import com.jselva.nisum.evaluationtest.data.models.response.ResponseGenericApiToken;
import com.jselva.nisum.evaluationtest.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping(value = "user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseGenericApi<UserDto>> register(@Valid @RequestBody UserRequestRegisterDto request) {
        ResponseGenericApi<UserDto> responseApi = new ResponseGenericApi<>();
        Optional<User> userOptional = this.userService.save(request);

        userOptional.ifPresent(user -> responseApi.setData(this.userService.convertToDto(user)));

        return ResponseEntity.status(HttpStatus.CREATED).body(responseApi);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseGenericApi<UserDto>> login(@Valid @RequestBody LoginRequest request) {
        ResponseGenericApi<UserDto> responseApi = new ResponseGenericApi<>();
        Optional<String> tokenOptional = this.userService.authenticate(request.getEmail(), request.getPassword());

        tokenOptional.ifPresent(token -> {
            var user = this.userService.find(request.getEmail());
            var dto = this.userService.convertToDto(user.orElseThrow(NullPointerException::new));
            responseApi.setData(dto);
        });

        return ResponseEntity.status(HttpStatus.OK).body(responseApi);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_READ')")
    @SecurityRequirement(name = "JWT")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseGenericApiToken<List<UserDto>>> getAll() {
        var responseApi = new ResponseGenericApiToken<List<UserDto>>();
        var users = this.userService.getAll();

        responseApi.setData(users.stream().map(this.userService::convertToDto).collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.OK).body(responseApi);
    }

}