package com.jselva.nisum.evaluationtest.services;

import com.jselva.nisum.evaluationtest.configuration.security.JwtProvider;
import com.jselva.nisum.evaluationtest.data.converter.UserConverter;
import com.jselva.nisum.evaluationtest.data.dto.UserDto;
import com.jselva.nisum.evaluationtest.data.entity.Role;
import com.jselva.nisum.evaluationtest.data.entity.User;
import com.jselva.nisum.evaluationtest.data.exceptions.ApiException;
import com.jselva.nisum.evaluationtest.data.models.RoleType;
import com.jselva.nisum.evaluationtest.data.repositories.PhoneRepository;
import com.jselva.nisum.evaluationtest.data.repositories.RoleRepository;
import com.jselva.nisum.evaluationtest.data.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final PhoneRepository phoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserConverter converter;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           RoleService roleService, PhoneRepository phoneRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;

        this.converter = new UserConverter();
    }

    @Transactional
    @Override
    public Optional<User> save(UserDto userDto) {
        Optional<User> userSaved = Optional.empty();
        var userExists = this.userRepository.findByEmail(userDto.getEmail());

        userExists.ifPresent(action -> {
            throw new ApiException("Ya existe un usuario registrado con ese email.", HttpStatus.FORBIDDEN);
        });

        if (!userDto.getPhones().isEmpty()) {
            var phoneOptionals = userDto.getPhones().stream()
                    .map(phone -> this.phoneRepository.findByNumber(phone.getNumber()))
                    .collect(Collectors.toList());

            phoneOptionals.forEach(
                    phoneOptional -> phoneOptional.ifPresent(
                            phone -> {
                                throw new ApiException("El telefono ingresado ya se encuentra registrado.", HttpStatus.FORBIDDEN);
                            }
                    )
            );
        }

        var role = this.roleRepository.findByName(RoleType.ROLE_READ.name());
        if (role.isPresent()) {
            userDto.setToken(this.jwtProvider.generateToken(userDto.getEmail(), Collections.singletonList(role.get())));
            userSaved = Optional.of(
                    this.userRepository.save(
                            convertToEntity(userDto, this.passwordEncoder.encode(userDto.getPassword()),
                                    userDto.getToken(), role.get())
                    )
            );
        }

        return userSaved;
    }

    @Override
    public Optional<User> find(String email) {
        var user = this.userRepository.findByEmail(email).orElseThrow(() ->
                new ApiException("user.not.found", HttpStatus.NOT_FOUND));

        return Optional.of(user);
    }

    @Transactional
    @Override
    public Optional<String> authenticate(String email, String password) {
        Optional<String> token = Optional.empty();
        Optional<User> userOptional = Optional.ofNullable(this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Usuario o contraseña invalidas", HttpStatus.NOT_FOUND)));

        if (userOptional.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                token = Optional.of(this.jwtProvider.generateToken(email, new ArrayList<>(userOptional.get().getRoles())));
                userOptional.get().setToken(token.get());
                userOptional.get().setActive(true);
                this.userRepository.save(userOptional.get());
            } catch (AuthenticationException e) {
                throw new ApiException("user.signin.failed", e, HttpStatus.UNAUTHORIZED);
            }
        }
        return token;
    }

    @Override
    public List<User> getAll() {
        var users = new ArrayList<User>();
        this.userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public void remove(String name) {
        throw new UnsupportedOperationException("Actualmente no se soporta la eliminación de usuarios.");
    }

    @Override
    public User convertToEntity(UserDto dto) {
        return this.converter.convertFromDto(dto);
    }

    public User convertToEntity(UserDto dto, String passwordEncoded, String token, Role role) {
        if (Objects.isNull(dto) || Objects.isNull(passwordEncoded)
                || Objects.isNull(token) || Objects.isNull(role)) {
            throw new IllegalArgumentException("Campos requeridos");
        }

        dto.setPassword(passwordEncoded);
        dto.setToken(token);
        dto.setRoles(Collections.singletonList(this.roleService.convertToDto(role)));

        return this.convertToEntity(dto);
    }

    @Override
    public UserDto convertToDto(User entity) {
        return this.converter.convertFromEntity(entity);
    }
}
